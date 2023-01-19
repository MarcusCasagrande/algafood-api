package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "estados") //HATEOAS
@Getter
@Setter
public class EstadoModel extends RepresentationModel<EstadoModel> {

    @ApiModelProperty(example = "1")
    private long id;

    @ApiModelProperty(example = "Santa Catarina")
    private String nome;
}
