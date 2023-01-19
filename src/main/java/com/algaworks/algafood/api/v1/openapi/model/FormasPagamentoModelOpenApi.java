package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.FormaPagamentoModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("FormasPagamentoModell")
@Setter
@Getter
public class FormasPagamentoModelOpenApi {// (19.43, desafio)

    private FPsEmbeddedModelOpenApi _embedded;
    private Links _links;

    @ApiModel("FormasPagamentoEmbeddedModell")
    @Data
    public class FPsEmbeddedModelOpenApi{
        private List<FormaPagamentoModel> LaFormasDelPagamentoHATEOAS_sim_renomeei_aqui;
    }
}
