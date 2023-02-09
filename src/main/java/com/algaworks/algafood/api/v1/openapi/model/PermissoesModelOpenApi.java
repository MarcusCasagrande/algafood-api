package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.PermissaoModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@Setter
@Getter
public class PermissoesModelOpenApi {// (19.44, desafio)

    private PermissoesEmbeddedModelOpenApi _embedded;
    private Links _links;

    @Data
    public class PermissoesEmbeddedModelOpenApi{
        private List<PermissaoModel> permissoesModel;
    }
}
