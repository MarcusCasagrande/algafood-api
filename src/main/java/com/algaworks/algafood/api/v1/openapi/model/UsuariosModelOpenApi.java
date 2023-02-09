package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.UsuarioModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;


@Setter
@Getter
public class UsuariosModelOpenApi {// (19.47, desafio)

    private UsuariosEmbeddedModelOpenApi _embedded;
    private Links _links;

    @Data
    public class UsuariosEmbeddedModelOpenApi{
        private List<UsuarioModel> usuariosModel;
    }
}
