package com.algaworks.algafood.api.v2.model;

import com.algaworks.algafood.api.v1.model.objectmodel.EstadoModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cidades")
@ApiModel("CidadeModel")
@Getter
@Setter
public class CidadeModelV2 extends RepresentationModel<CidadeModelV2> { //aula 19.11

    //@ApiModelProperty(value = "ID da cidade", example = "1")
    @ApiModelProperty(example = "1") // pra example value na documentacao
    private Long idCidade;

    @ApiModelProperty(example = "Içara")
    private String nomeCidade;

    private Long idEstado;
    private String nomeEstado;


//    private EstadoModel estado;

}
