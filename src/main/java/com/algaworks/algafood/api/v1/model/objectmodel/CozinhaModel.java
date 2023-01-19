package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cozinhas") //HATEOAS
@ApiModel(value = "ElCozinaa", description = "Representa uma cozinha")
@Setter
@Getter
public class CozinhaModel extends RepresentationModel<CozinhaModel> {

    @ApiModelProperty(example = "1")
//    @JsonView(RestauranteView.Resumo.class)//JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    private Long id;

    @ApiModelProperty(example = "Thay")
//    @JsonView(RestauranteView.Resumo.class)//JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    private String nome;
}
