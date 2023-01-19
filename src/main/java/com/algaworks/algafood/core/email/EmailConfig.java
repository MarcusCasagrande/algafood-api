package com.algaworks.algafood.core.email;

import com.algaworks.algafood.domain.service.EnvioEmailService;
import com.algaworks.algafood.infrastructure.service.email.FakeEnvioEmailService;
import com.algaworks.algafood.infrastructure.service.email.SandboxEmailService;
import com.algaworks.algafood.infrastructure.service.email.SmtpEnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Autowired
    private EmailProperties properties;

    @Bean
    public EnvioEmailService envioEmailService(){
        switch (properties.getImpl()) {
            case fake:
                return new FakeEnvioEmailService();
            case smtp:
                return new SmtpEnvioEmailService();
            case sandbox:
                return new SandboxEmailService();
            default:
                return null;
        }
    }
}
