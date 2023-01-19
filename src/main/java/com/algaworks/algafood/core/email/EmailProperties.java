package com.algaworks.algafood.core.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated //define que a classe é validada
@Getter
@Setter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {

    //algafood.email.remetente=Marcus-food <marcus.r.casagrande@gmail.com>
    @NotNull // se a aplicacao iniciar sem essa prop, ela nem inicia.
    private String remetente;
    private TipoImpl impl = TipoImpl.smtp; // default
    private Sandbox sandbox = new Sandbox();

    public enum TipoImpl{
        smtp, fake, sandbox;
    }

    //algafood.email.sandbox.destinatario=marcus.xj220@gmail.com
    @Getter
    @Setter
    public class Sandbox {
        private String destinatario;
    }
}
