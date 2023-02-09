package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cozinhas") //HATEOAS
@Setter
@Getter
public class CozinhaModel extends RepresentationModel<CozinhaModel> {

//    @JsonView(RestauranteView.Resumo.class)//JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    @Schema(example = "1")
    private Long id;

//    @JsonView(RestauranteView.Resumo.class)//JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    @Schema(example = "Tailandesa")
    private String nome;
}
