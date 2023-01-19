package com.algaworks.algafood.infrastructure.service.email;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;

//@Service // comentado, pois vai ser beanizado de outra forma (EmailConfig.java)
public class SmtpEnvioEmailService implements EnvioEmailService {

    @Autowired
    protected JavaMailSender mailSender;

    @Autowired
    protected EmailProperties emailProperties;

    @Autowired
    private Configuration freemarkerConfig;

//    @Autowired
//    private Environment env;

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
            helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
            helper.setFrom(emailProperties.getRemetente());
            mailSender.send(mimeMessage);
        } catch (Exception e){
            throw new EmailException("Não foi possível enviar e-mail", e);
        }
    }

    protected String processarTemplate(Mensagem mensagem){
        try {
            Template template = freemarkerConfig.getTemplate(mensagem.getCorpo());
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, mensagem.getVariaveis());
        } catch (Exception e) {
            throw new EmailException("Não foi possível montar o template do e-mail", e);
        }
    }
}
