package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.GrupoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("GruposModell")
@Setter
@Getter
public class GruposModelOpenApi {// (19.44, desafio)

    private GruposEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("gruposEmbeddedModell")
    @Data
    public class GruposEmbeddedModelOpenApi{
        private List<GrupoModel> grupoModels;
    }
}
