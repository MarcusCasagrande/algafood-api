package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "fotosRel")
@Getter
@Setter
public class FotoProdutoModel extends RepresentationModel<FotoProdutoModel> {

    @Schema(example = "(UUID)_foto.png")
    private String nomeArquivo;

    @Schema(example = "Bisteca assada")
    private String descricao;

    @Schema(example = "image/png")
    private String contentType;

    @Schema(example = "202912")
    private Long tamanho;
}
