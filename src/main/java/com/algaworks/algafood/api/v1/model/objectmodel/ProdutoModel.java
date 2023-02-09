package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Relation(collectionRelation = "produtosRel")
@Getter
@Setter
public class ProdutoModel extends RepresentationModel<ProdutoModel> {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Batata")
    private String nome;

    @Schema(example = "batata frita")
    private String descricao;

    @Schema(example = "10.00")
    private BigDecimal preco;

    @Schema(example = "true")
    private Boolean ativo;
}
