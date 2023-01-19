package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "LaCidadee")
@Getter
@Setter
public class CidadeResumoModel extends RepresentationModel<CidadeResumoModel> {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "Içara")
    private String nome;

    @ApiModelProperty(example = "SC")
    private String estado; // se fosse nomeEstado, atribuiria da forma correta. Porém, do jeito que ta, o model mapper atribui um Estado.toString() (id=1, descrixao= xxx..). entao o modelMapperConfig aruma isso

}
