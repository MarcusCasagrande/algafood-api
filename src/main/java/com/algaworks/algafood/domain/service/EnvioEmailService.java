package com.algaworks.algafood.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Map;
import java.util.Set;

public interface EnvioEmailService {

    void enviar(Mensagem mensagem);

    @Getter
    @Builder
    class Mensagem {

        @Singular // "singulariza" a variavel abaixo (permite ".setDestinatario" ao inves de ".setDestinatarioS")
        private Set<String> destinatarios;

        @NonNull // obriga o builder disso a informar o campo abaixo (EnvioEmailService.Mensagem.builder().assunto()...)
        private String assunto;

        @NonNull
        private String corpo;

        @Singular("variavel") // param pra definir o singluar disso, pois automaticamente ele nao detectou
        private Map<String, Object> variaveis;
    }
}
