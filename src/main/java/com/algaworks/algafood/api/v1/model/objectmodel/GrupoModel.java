package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "LosGrupos")
@Getter
@Setter
public class GrupoModel extends RepresentationModel<GrupoModel> {

    // position: só testando
    @Schema(example = "1")
    private Long id;

    @Schema(example = "administração")
    private String nome;
}
