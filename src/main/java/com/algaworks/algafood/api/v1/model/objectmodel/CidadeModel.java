package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cidades") // nome do array do JSON que contem o listar() /cidades tem seu nome de array mudado do padrão "cidadeModelList" para o que ta anotado ali (Aula 19.10)
@Getter
@Setter
public class CidadeModel extends RepresentationModel<CidadeModel> { //aula 19.11

    //@ApiModelProperty(value = "ID da cidade", example = "1")
    @Schema(example = "1") // 26.9
    private Long id;

    @Schema(example = "Uberlandia")
    private String nome;


    private EstadoModel estado;

}
