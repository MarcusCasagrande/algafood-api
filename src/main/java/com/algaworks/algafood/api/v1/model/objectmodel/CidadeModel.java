package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cidades") // nome do array do JSON que conem o listar() /cidades tem seu nome de array mudado do padrão "cidadeModelList" para o que ta anotado ali (Aula 19.10)
@ApiModel(value = "Cidadee", description = "Representa uma cidade") // faz com que na documentacao, essa reprsentacoa mude de nome
@Getter
@Setter
public class CidadeModel extends RepresentationModel<CidadeModel> { //aula 19.11

    //@ApiModelProperty(value = "ID da cidade", example = "1")
    @ApiModelProperty(example = "1") // pra example value na documentacao
    private Long id;

    @ApiModelProperty(example = "Içara")
    private String nome;
    private EstadoModel estado;

}
