package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Relation(collectionRelation = "LosRestaurantesBasic")
@Getter
@Setter
public class RestauranteBasicoModel extends RepresentationModel<RestauranteBasicoModel> {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "tuc-tuc")
    private String nome;

    @Schema(example = "1.50")
    private BigDecimal taxaFrete;

    private CozinhaModel cozinha;
}
