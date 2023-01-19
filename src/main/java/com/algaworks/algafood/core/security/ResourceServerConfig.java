package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.spec.SecretKeySpec;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration // redundante, pois @EnableWebSecurity ja tem isso
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 23.21
public class ResourceServerConfig extends WebSecurityConfigurerAdapter { // nome custom. Classe de configuracao de security (22.3)

    // nao precisa mais configurar, pois quem faz o authentication agora é o auth-server (22.11)
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // usando usuarios in memoria (22.4)
//        auth.inMemoryAuthentication()
//                .withUser("thiago")
//                .password(passwordEncoder().encode("123"))
//                .roles("ADMIN")
//            .and()
//                .withUser("joao")
//                .password(passwordEncoder().encode("123"))
//                .roles("ADMIN");
//    }

//    @Override // era do basic auth, metodo abaixo agora faz com spring security
//    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic()// sem formulario
//                .and().authorizeRequests()
//                    .antMatchers("/v1/cozinhas/**").permitAll() // tudo que for v1/cozinhas é permitido sem auth
//                    .anyRequest().authenticated()
//                .and().sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // nao tem mais cookies, fica stateless (20.3)
//                .and().csrf().disable();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    //23.21: authorize requests removidos, pra se usar @PreAuthorize e SpEL
//            .authorizeRequests() //23.20 setando os grants
//                .antMatchers(HttpMethod.POST, "/v1/cozinhas/**").hasAuthority("EDITAR_COZINHAS")
//                .antMatchers(HttpMethod.PUT, "/v1/cozinhas/**").hasAuthority("EDITAR_COZINHAS")
//                .antMatchers(HttpMethod.GET, "/v1/cozinhas/**").authenticated()
//                .anyRequest().denyAll()
////                .anyRequest().authenticated()
//            .and()// pra entender que esse é um resource server de fato usando oauth2
                .formLogin() // 23.41: indica q há o formulario de login (do authorization_code fluxo)
                .loginPage("/login") //23.43 pagina que pode ser customizada
                .and()
                .authorizeRequests()
                    .antMatchers("/oauth/**").authenticated() // 23.41: configura que caminhos /oauth precisa estar autenticado, que é o que ocorre no url usada no fluxo authorization_code. Senão no console dá: "User must be authenticated with Spring Security before authorization can be completed."
                .and()
                .csrf().disable() // introduzido na aula 23.21, nao lembro exatamente a funcao
                .cors().and()// (22.19) spring ao usar seguranca ja protege todos os endpoints por padrao. Por isso o preflight do OPTIONS nao funcionam mais (relembrar aulas anteriores). Essa linha permite o metodo OPTIONS novamente nessa app
//                .oauth2ResourceServer().opaqueToken();
                .oauth2ResourceServer().jwt() //23.6
                .jwtAuthenticationConverter(jwtAuthenticationConverter()); // 23.20: define que vai verificar os authorities do jwt
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter(){
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwt.getClaimAsStringList("authorities"); // pega o clain "authorities" do jwt e transforma numa lista de string
            if (authorities == null){
                authorities = Collections.emptyList();
            }
            var scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter(); // 23.25: pegar scopes
            var grantedAuthorities = scopesAuthoritiesConverter.convert(jwt);
            grantedAuthorities.addAll(authorities.stream() // transforma essa lista de string em SimpleGrantedAuth
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
            return grantedAuthorities;
        });
        return jwtAuthenticationConverter;
    }

    @Bean
    @Override //registrando authenticationManager como um Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // (23.12) codigo removido pois agora se usa jwt com arquivo de public key (nao usa mais chave simetrica)
//    @Bean // 23.6
//    public JwtDecoder jwtDecoder(){
//        var secretKey = new SecretKeySpec("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456".getBytes(), "HmacSHA256"); //min de 32 bytes
//        return NimbusJwtDecoder.withSecretKey(secretKey).build();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){ // password enconder necessario pra funcionar os users in memory (22.4)
//        return new BCryptPasswordEncoder();
//    }
}
