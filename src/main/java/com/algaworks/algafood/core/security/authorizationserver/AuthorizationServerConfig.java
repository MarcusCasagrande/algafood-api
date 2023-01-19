package com.algaworks.algafood.core.security.authorizationserver;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer // permite que a classe sekja um authorization server (22.8)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // agora que vem de JDBC, nao é mais usado (23.36)
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    JwtKeyStoreProperties jwtKeyStoreProperties;

//    @Autowired //redis stuff)
//    private RedisConnectionFactory redisConnectionFactor;

    @Autowired
    private DataSource dataSource;

    @Override // configurar detalhes dos clientes (app/device que vai acessar o resource server) do authorization server (22.9)
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource); // provavelmente ja sabe aqui a tabela a ser pesquisada: (oauth_client_details)
//        clients.inMemory()
//                .withClient("algafood-web") // especifica a identificacao do cliente (pode ser web, mobile, desktop...)
//                    .secret(passwordEncoder.encode("web123"))
//                    .authorizedGrantTypes("password", "refresh_token") //usa apenas o fluxo password. Refresh token aula (22.13)
//                    .scopes("WRITE", "READ")
//                .accessTokenValiditySeconds(60*60*6)
//                .refreshTokenValiditySeconds(20) // validade do refresh token (22.14)
//            .and()// aplicacao back-end, sem authentication de user (22.16)
//                .withClient("faturamento")
//                    .secret(passwordEncoder.encode("faturamento123"))
//                    .authorizedGrantTypes("client_credentials")
//                    .scopes("READ")
//            .and() // authorization code grand type (22.18)
//                // url browser: http://localhost:8080/oauth/authorize?response_type=code&client_id=foodanalytics&state=abc&redirect_uri=http://localhost:8082
//                .withClient("foodanalytics")
//                .secret(passwordEncoder.encode("")) // com PKCE nao precisa de secret
//                // 22.23: com o codigo de extensao ao PKCE, o authorization_code agora suporta isso, sem mudar nada nesse client aqui.
//                // 22.24: nova url agora com PKCE: http://localhost:8081/oauth/authorize?response_type=code&client_id=foodanalytics&state=abc&redirect_uri=http://localhost:8082&code_challenge=KJFg2w2fOfmuF1TE7JwW-QtQ4y4JxftUga5kKz09GjY&code_challenge_method=s256 (s256 ou plain) // 22.25: code challenge de "teste123": KJFg2w2fOfmuF1TE7JwW-QtQ4y4JxftUga5kKz09GjY
//                .authorizedGrantTypes("authorization_code") // poderia ter refreshtoken aqui
//                .scopes("WRITE", "READ", "lalala")
//                .redirectUris("http://aplicacao-cliente")
//            .and() // 22.21: implicit grant (nao recomendado, prefira o auth code acima)
//                    // url browser: http://localhost:8081/oauth/authorize?response_type=token&client_id=webadmin&state=abc&redirect_uri=http://localhost:8082
//                    // usa o token conseguido via browser pra acessar diretamente os recursos de cozinha
//                .withClient("webadmin") // .secret nao necessario
//                .authorizedGrantTypes("implicit") // nao usa refresh token
//                .scopes("WRITE", "READ")
//                .redirectUris("http://aplicacao-cliente");
    }

    @Override // metodo de check-token pra ver se é válido (22.10)
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //security.checkTokenAccess("isAuthenticated()");
        security.checkTokenAccess("permitAll()") // checa o token e permite uso mesmo sem enviar os dados do usuario (sem authenticar o client) no basic-auth (nao precisaria mais mandar user e pass pra ca no endpoint oauth/check_token)
                .allowFormAuthenticationForClients() // 22.24: permite que o client_id possa ser passado via chave no x-www-form, ao inves de por basic auth no authorization (do postman). Authorization code grant era pra ser assim por padrao, mas tem que fazer isso, aparentemente, por falta de suporte no spring oauth
                .tokenKeyAccess("permitAll()"); // (23.11) permite que se faça acesso GET pra pegar a chave publica
    }

    @Override //somente o "password" flow precisa desse authenticationManager
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        var enhancerChain = new TokenEnhancerChain(); // 23.16: setando token enhancers
        enhancerChain.setTokenEnhancers(Arrays.asList(new JwtCustomClaimsTokenEnhancer(), jwtAccessTokenConverter())); // JwtCustomClaimsTokenEnhancer deve ser o primeiro, nao sei o motivo

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService) //22.13
                .reuseRefreshTokens(false) // nao reusa o mesmo refresh token (22.14)
                .tokenGranter(tokenGranter(endpoints)) // 22.23: adiciona a implementacao do PKCE nos granters possiveis (codigo abaixo)
                .accessTokenConverter(jwtAccessTokenConverter()) // 23.5: tokens agora sao jwt ao inves de opaque tokens
                // redis stuff: .tokenStore(new RedisTokenStore(redisConnectionFactory)); // token store agora nao é mais in-memory, é via Redis (23.2)
                .approvalStore(approvalStore(endpoints.getTokenStore())) // (23.13) accessTokenConverter (logo acima) cria um token store. E essa linha configura esse token store pra dar ao cliente as opcoes de escopo de acesso (read write) dos endpoints.
                .tokenEnhancer(enhancerChain); //23.16
    }

    private ApprovalStore approvalStore(TokenStore tokenStore){
        var approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(tokenStore);
        return null;
    }

    // 23.45 bean pra criar o JWKS
    @Bean
    public JWKSet jkwSet(){
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair().getPublic()) // pega a chave publica de dentro do nosso par de chaves (ate entao o arquivo algafood.jks)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("algafood-key-id"); //identificar cada key no keyset (aqui só teremos uma)
        return new JWKSet(builder.build());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        var jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //jwtAccessTokenConverter.setSigningKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456"); // min 32 bites (assinatura simetrica)

        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    private KeyPair keyPair(){
        //chave assimetrica (23.9)
        var keyStorePass = jwtKeyStoreProperties.getPassword();
        var keyPairAlias = jwtKeyStoreProperties.getKeypairAlias();
        var keyStoreKeyFactory = new KeyStoreKeyFactory(jwtKeyStoreProperties.getJksLocation(), keyStorePass.toCharArray()); // 23.41: jwtKeyStoreProperties.getJksLocation() (no authserver era diferente)
        return keyStoreKeyFactory.getKeyPair(keyPairAlias);
    }

    // adiciona o PKCE aos grants possiveis (22.23). Codigo de terceiro dessa aula
    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
                endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());

        var granters = Arrays.asList(
                pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());

        return new CompositeTokenGranter(granters);
    }
}
