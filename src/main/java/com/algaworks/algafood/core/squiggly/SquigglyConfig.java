package com.algaworks.algafood.core.squiggly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SquigglyConfig {

    @Bean
    public FilterRegistrationBean<SquigglyRequestFilter> squigglyRequestFilter(ObjectMapper objectMapper){
        Squiggly.init(objectMapper, new RequestSquigglyContextProvider("campos", null)); // padrao de filter: "fields". Se botar argumento no new Request("campos", null).. dae muda o padrao pra "campos" ao inves de "fields"

        // por padrao, o filtro funciona em todo projeto. Nesta linha, é definido que só funciona para as urls selecionadas
        var urlPatters = Arrays.asList("/pedidos/*", "/restaurantes/*");

        var filterRegistration = new FilterRegistrationBean<SquigglyRequestFilter>(); //classe vem de Filter do JavaEE
        filterRegistration.setFilter(new SquigglyRequestFilter());
        filterRegistration.setOrder(1);
        filterRegistration.setUrlPatterns(urlPatters); // pra setar apenas nas urls definidas acima

        return filterRegistration; // RESUMO da coisa: O que for posto em queryparams da url que tenha o nome "campos", será filtrado pelo squiggly
    }
}
