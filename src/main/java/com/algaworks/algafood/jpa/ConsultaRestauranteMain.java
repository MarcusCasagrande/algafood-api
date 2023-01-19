package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ConsultaRestauranteMain {

    public static void main(String[] args) {
        ApplicationContext app = new SpringApplicationBuilder(AlgafoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        RestauranteRepository cadastroRestaurante = app.getBean(RestauranteRepository.class);
        List<Restaurante> restaurantes = cadastroRestaurante.findAll();
        for (Restaurante z : restaurantes){
            System.out.printf("%s - %f - %s\n", z.getNome(), z.getTaxaFrete(), z.getCozinha().getNome());
        }
    }
}
