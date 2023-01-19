package com.algaworks.algafood.core.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired // injetando o intercepctador depreciador pra ser usado como msg pra /v1/'s. (20.18)
    private ApiRetirementHandler apiDeprecationHandler;

    //23.41: removido. Pois ja tem cors na classe Corsconfig
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // CORS habilitado globalmente pra todos os tipos de requests
//                //.allowedOrigins("http://localhost") // o padrao ja permite de todas as origens
//                //.allowedMethods("GET", "HEAD", "POST") // PADRAO, se nao pusermos nada
//                .allowedMethods("*"); // TODOS
//                //.maxAge(15) MAXAGE de preflight em cache no navegador (ver 16.5)
//    }

    // para criar ETag filters (aula 17.5)
    // tbm funcionaria "inline" durante o:
    //ResponseEntity.ok().etag("..."). Mas vais er feito dessa forma aqui
    @Bean
    public Filter shallowEtagHeaderFilter(){
        return new ShallowEtagHeaderFilter(); // alem de gear o etag, verifica se o etag que veio na request é igual. E caso sim, devolve um 304 (not modified)
    }

    @Override // 20.18
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiDeprecationHandler);
    }
//    @Override //especifica que o contentType padrão a ser buscado, caso o user nao defina o Accept, seja o vnd.algafood.v2+json (20.12)
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer.defaultContentType(AlgaMediaTypes.V2_APPLICATION_JSON);
//    }
}
