package com.algaworks.algafood.api.v2.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.CidadeModel;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("CidadesModellw")
@Data
public class CidadesModelOpenApiV2 {

    private CidadeEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("CidadesEmbeddedModell")
    @Data
    public class CidadeEmbeddedModelOpenApi{
        private List<CidadeModelV2> cidades;
    }
}
