package com.algaworks.algafood.api.v2;

import com.algaworks.algafood.api.v1.controller.CozinhaController;
import com.algaworks.algafood.api.v2.controller.CidadeControllerV2;
import com.algaworks.algafood.api.v2.controller.CozinhaControllerV2;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Component
public class AlgaLinksV2 {

    private static final String SELF = IanaLinkRelations.SELF.value();

    public Link linkToCidades(){
        return linkToCidades(SELF);
    }
    public Link linkToCidades(String rel){
        return linkTo(CidadeControllerV2.class).withRel(rel);
    }

    public Link linkToCozinhas(){
        return linkToCozinhas(SELF);
    }
    public Link linkToCozinhas(String rel){
        return linkTo(CozinhaControllerV2.class).withRel(rel);
    }

}
