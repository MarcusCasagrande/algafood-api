package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "usuarrios")
@Setter
@Getter
public class UsuarioModel extends RepresentationModel<UsuarioModel> {

    @Schema(example = "1")
    private long id;

    @Schema(example = "João da Silva")
    private String nome;

    @Schema(example = "a@b.com")
    private String email;
}
