package com.algaworks.algafood.core.squiggly;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * classe 3rd party. Serve para configurar o tomcat do spring. Neste caso, simplesmente para que a URL aceite os caracters colchetes ao inves de ter que usar %5B e %5D. (Visto na aula de Squiggly)
 */
@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> connector.setAttribute("relaxedQueryChars", "[]"));
    }

}
