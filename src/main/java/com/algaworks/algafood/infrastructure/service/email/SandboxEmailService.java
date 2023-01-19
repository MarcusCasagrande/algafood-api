package com.algaworks.algafood.infrastructure.service.email;


import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

//@Slf4j // serve pra logar, algo assim
public class SandboxEmailService extends SmtpEnvioEmailService {

    @Override
    public void enviar(Mensagem mensagem) {
        try {
            String corpo = processarTemplate(mensagem);
            // mimemessage = mensagem que queremos enviar. Deve-se atribuir varias coisas (como sender, subject, body...)
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            // helper que ajuda a configurar o mimeMessage
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setSubject(mensagem.getAssunto());
            helper.setText(corpo, true); // true define que é um texto em HTML, e nao texto puro. Padrão hoje em dia
            helper.setTo(emailProperties.getSandbox().getDestinatario());
            helper.setFrom(emailProperties.getRemetente());
            mailSender.send(mimeMessage);
        } catch (Exception e){
            throw new EmailException("Não foi possível enviar e-mail", e);
        }
    }
}
