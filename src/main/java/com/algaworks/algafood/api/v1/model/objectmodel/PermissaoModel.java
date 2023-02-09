package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "Laspermissoes")
@Getter
@Setter
public class PermissaoModel extends RepresentationModel<PermissaoModel> {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "CONSULTAR_COZINHAS")
    private String nome;

    @Schema(example = "Permite consultar cozinhas")
    private String descricao;
}
