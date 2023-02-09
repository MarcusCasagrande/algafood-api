package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.GrupoModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@Setter
@Getter
public class GruposModelOpenApi {// (19.44, desafio)

    private GruposEmbeddedModelOpenApi _embedded;
    private Links _links;

    @Data
    public class GruposEmbeddedModelOpenApi{
        private List<GrupoModel> grupoModels;
    }
}
