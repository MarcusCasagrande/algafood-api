package com.algaworks.algafood.infrastructure.service.email;


import lombok.extern.slf4j.Slf4j;

@Slf4j // serve pra logar, algo assim
public class FakeEnvioEmailService extends SmtpEnvioEmailService {

    @Override
    public void enviar(Mensagem mensagem) {
//        log.info("Assunto do e-mail: " + mensagem.getAssunto());
//        log.info("Arquivo do corpo: " + mensagem.getCorpo());
        // Foi necessário alterar o modificador de acesso do método processarTemplate
        // da classe pai para "protected", para poder chamar aqui
        String corpo = processarTemplate(mensagem);

        log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);
    }
}
