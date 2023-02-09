package com.algaworks.algafood.core.security.authorizationserver;

import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration //27.3 nao precisa extender/implementar nada. No SAS é tudo via @Bean
public class AuthorizationServerConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // teremos varios filter chain. E precisa garantir que o do AuthServer seja feito antes do Resourcer server
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        //OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 27.18: Sobreescrita da implementacao do metodo 'applyDefaultSecurity' acima, pois é a unica forma atual de customizar o "roteamento" para a pagina custom de approval de permissoes
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

        authorizationServerConfigurer.authorizationEndpoint(customizer -> customizer.consentPage("/oauth2/consent")); //unica linha acrescentada

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        http.requestMatcher(endpointsMatcher)
            .authorizeRequests(authorizeRequests ->
                    authorizeRequests.anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
            .apply(authorizationServerConfigurer);

        return http
                //.formLogin(Customizer.withDefaults()) // 27.11: pra permitir a tela de form login PADRÃO na aplicacao

                .formLogin(customizer -> customizer.loginPage("/login")) //27.17: custom page
                .build();
    }

    @Bean // responsavel por determinar quem vai ser o authServer que vai assinar os tokens (nesse caso, localhost aqui mesmo)
    public ProviderSettings providerSettings(AlgaFoodSecurityProperties properties) {
        return ProviderSettings.builder()
                .issuer(properties.getProviderUrl()) // issuer é a url do AuthServer
                .build();
    }
    @Bean //Bean que vai guardar os clients do AuthServer (inMemory, JDBC, Regis..)
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder, JdbcOperations jdbcOperations) {//27.16: jdbcOperations injetado aqui pra poder retornar em jdbc ao inves de in-memory
        /**
         * TUDO APAGADO POIS VAI SER TUDO EDITADO VIA SQL MIGRATION
        RegisteredClient algafoodbackend = RegisteredClient.withId("1") // id da base onde vai ser colocado, e nao o Id do client
                .clientId("algafood-backend")
                .clientSecret(passwordEncoder.encode("backend123"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // metodo de seguranca BASIC
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("READ")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) //REFERENCE: token Opaco. SELF_CONTAINED seria o jwt
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .build())
                .build();

        // 27.11: criando cliend web
        RegisteredClient algafoodweb = RegisteredClient.withId("2")
                .clientId("algafood-web")
                .clientSecret(passwordEncoder.encode("web123"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) //27.13: novo grant type incluido tambem, o do refresh token
                .scope("READ").scope("WRITE")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT
                        .accessTokenTimeToLive(Duration.ofMinutes(15))
                        .reuseRefreshTokens(false)
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .build())
                // 27.12: localhost trocado por ip pra nao dar o erro no form:  localhost is not allowed for the redirect_uri (http://localhost:8080/authorized). Use the IP literal (127.0.0.1) instead.
                .redirectUri("http://127.0.0.1:8080/authorized") //custom uri. Nao existe. Só usada pra voltar o code
                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html") // essa existe. Do swagger, usada pra recuperar o code
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true) // obrigatorio aparecer a tela de consentimento (form)
                        .build())
                .build();

        RegisteredClient foodanalytics = RegisteredClient.withId("3")
                .clientId("foodanalytics")
                .clientSecret(passwordEncoder.encode("web123"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("READ").scope("WRITE")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .build())
                .redirectUri("http://www.foodanalytics.local:8082") //custom uri. Nao existe. Só usada pra voltar o code
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false) // foodanalytics é false
                        .build())
                .build();

        //return new InMemoryRegisteredClientRepository(Arrays.asList(algafoodbackend, algafoodweb, foodanalytics)); // 27.16: mudado de in-memory pra JDBC
        JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(jdbcOperations);
        repository.save(algafoodbackend);
        repository.save(algafoodweb);
        repository.save(foodanalytics);
        return repository;
        */
        return new JdbcRegisteredClientRepository(jdbcOperations);
    }

    @Bean // 27.7: pra registrar os client LOGADOS via jdbc (o token e expiracao atual de quem ta on)
    public OAuth2AuthorizationService oAuth2AuthorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository rcr) { // injeta o Bean acima (rcr) aqui
        return new JdbcOAuth2AuthorizationService(jdbcOperations,rcr);
    }

    @Bean // 27.9: carregar par de chave pra gerar o jwt
    public JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties properties) throws Exception {
        char[] keyStorePass = properties.getPassword().toCharArray();
        String keypairAlias = properties.getKeypairAlias();
        Resource jksLocation = properties.getJksLocation();// location é no momento em base64 no app.properties
        InputStream inputStream = jksLocation.getInputStream();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, keyStorePass);
        RSAKey rsaKey = RSAKey.load(keyStore, keypairAlias, keyStorePass);
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean // 27.14: Setando dados de authorities no token jwt do resource server
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UsuarioRepository usuarioRepository) {
        return context -> {
            Authentication authentication = context.getPrincipal();
            if (authentication.getPrincipal() instanceof User) { // nem t.odo fluxo vai ser um User. Por exemplo, client-credentials não precisa de user
                User user = (User) authentication.getPrincipal();
                Usuario usuario = usuarioRepository.findByEmail(user.getUsername()).orElseThrow();
                Set<String> authorities = new HashSet<>();
                for (GrantedAuthority authority : user.getAuthorities()) { // pra pegar os authorities (EDITAR_COZINHAS)
                    authorities.add(authority.getAuthority());
                }
                context.getClaims() //acessando token do usuario antes mesmo dele ser criado, segundo a aula, pra dar os claims pq depois de criado um token jwt nao pode ser alterado
                        .claim("usuario_id", usuario.getId().toString()); // toString pq é assim que vai pra base de dados
                context.getClaims().claim("authorities", authorities);
            }
        };
    }

    @Bean // 27.18 persistir em memoria as autorizacoes de consentimento (allow/deny READ)
    public OAuth2AuthorizationConsentService consentService(JdbcOperations jdbcOperations, RegisteredClientRepository clientRepository){ //27.19: adicionando esses params pra persistir em base de dados ao inves de in-memory
        //return new InMemoryOAuth2AuthorizationConsentService();
        return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, clientRepository);
    }

    @Bean //27.20
     public OAuth2AuthorizationQueryService auth2AuthorizationQueryService(JdbcOperations jdbcOperations, RegisteredClientRepository repository){
        return new JdbcOAuth2AuthorizationQueryService(jdbcOperations, repository);
    }

}
