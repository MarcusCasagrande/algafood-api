package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "LosRestaurantesResumo")
@Getter
@Setter
public class RestauranteIdNomeModel extends RepresentationModel<RestauranteIdNomeModel> {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "Thay")
    private String nome;
}
