package com.algaworks.algafood.core.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.http.MediaType;

@Configuration // classe utilitaria para corrigir problemas do formato HAL do hateoas, que deturpa os objetos de links quando usado um mediaType customizado (20.10)
public class AlgaHalConfiguration {
    // classe deletada na aula 20.13, pois acesso de versao via mediatype nao foi mais usado
    @Bean
    public HalConfiguration globalPolicy() {
        return new HalConfiguration()
                .withMediaType(MediaType.APPLICATION_JSON)
                .withMediaType(AlgaMediaTypes.V1_APPLICATION_JSON)
                .withMediaType(AlgaMediaTypes.V2_APPLICATION_JSON);
    }

}