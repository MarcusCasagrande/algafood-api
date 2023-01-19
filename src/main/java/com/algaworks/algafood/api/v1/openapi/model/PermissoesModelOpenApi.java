package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.PermissaoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("PermissoesModell")
@Setter
@Getter
public class PermissoesModelOpenApi {// (19.44, desafio)

    private PermissoesEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("permissoesEmbeddedModell")
    @Data
    public class PermissoesEmbeddedModelOpenApi{
        private List<PermissaoModel> permissoesModel;
    }
}
