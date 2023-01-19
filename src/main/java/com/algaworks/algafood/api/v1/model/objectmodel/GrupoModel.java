package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "LosGrupos")
@ApiModel(value = "Grupoo", description = "Representa um grupo de userss") // faz com que na documentacao, essa reprsentacoa mude de nome
@Getter
@Setter
public class GrupoModel extends RepresentationModel<GrupoModel> {

    // position: só testando
    @ApiModelProperty(example = "1", position = 98)
    private Long id;

    @ApiModelProperty(example = "Gerentey", position = 99)
    private String nome;
}
