package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "LosRestaurantesResumo")
@Getter
@Setter
public class RestauranteIdNomeModel extends RepresentationModel<RestauranteIdNomeModel> {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Tuctuc")
    private String nome;
}
