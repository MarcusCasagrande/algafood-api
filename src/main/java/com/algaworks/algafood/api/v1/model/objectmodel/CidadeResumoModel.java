package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "LaCidadee")
@Getter
@Setter
public class CidadeResumoModel extends RepresentationModel<CidadeResumoModel> {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Içara")
    private String nome;

    @Schema(example = "SC")
    private String estado; // se fosse nomeEstado, atribuiria da forma correta. Porém, do jeito que ta, o model mapper atribui um Estado.toString() (id=1, descrixao= xxx..). entao o modelMapperConfig aruma isso

}
