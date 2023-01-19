package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "LaFormasDelPagamentoHATEOAS")
@ApiModel(value = "ElPagto", description = "Representa uma forma de pagamento")
@Setter
@Getter
public class FormaPagamentoModel extends RepresentationModel<FormaPagamentoModel> {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "Crédito")
    private String descricao;
}
