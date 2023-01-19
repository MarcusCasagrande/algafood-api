package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.UsuarioModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("UsuariosModell")
@Setter
@Getter
public class UsuariosModelOpenApi {// (19.47, desafio)

    private UsuariosEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("usuariosEmbeddedModell")
    @Data
    public class UsuariosEmbeddedModelOpenApi{
        private List<UsuarioModel> usuariosModel;
    }
}
