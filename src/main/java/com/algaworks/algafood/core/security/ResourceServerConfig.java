package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration //27.6 lembrando: SAS nao implementa nada. Tudo vem por @Bean
@EnableGlobalMethodSecurity(prePostEnabled = true) // pras config de "pos filter" e "pre filter" funcionarem
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/oauth2/**").authenticated()
            .and()
            .csrf().disable()
            .cors().and()
            //.oauth2ResourceServer().opaqueToken();
            .oauth2ResourceServer().jwt() //27.10
                .jwtAuthenticationConverter(jwtAuthenticationConverter()); //27.15
        return http
                //.formLogin(Customizer.withDefaults()) // 27.11: pra permitir a tela de form login na aplicacao
                .formLogin(customizer -> customizer.loginPage("/login")) //27.17: custom page. Caso o resource server seja separado do auth server, essa configuracao APARENTEMENTE nao precisa ser feita
                .build();
    }

    // 27.15: pro resource server ler as permissoes (EDITAR_COZINHA) e authorizar os end points
    private JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorities = jwt.getClaimAsStringList("authorities");
            if (authorities == null){
                return Collections.emptyList(); // pra nao dar erro de serializacao
            }
            JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter(); // este objeto irá converter os scopes do JWT em Authority
            Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt); // aqui vem dos scopes
            grantedAuthorities.addAll(authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())); // junta os scopes com a lista de authorities
            return grantedAuthorities;
        });
        return converter;
    }
}
