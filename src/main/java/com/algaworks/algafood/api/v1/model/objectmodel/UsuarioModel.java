package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "usuarrios")
@Setter
@Getter
public class UsuarioModel extends RepresentationModel<UsuarioModel> {

    @ApiModelProperty(example = "1")
    private long id;

    @ApiModelProperty(example = "José da silva")
    private String nome;

    @ApiModelProperty(example = "alala@gmail.com")
    private String email;
}
