package com.algaworks.algafood.api.v1.model.input;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class ItemPedidoInput {

    @NotNull
    @Schema(example = "1")
    private Long produtoId; //ver modelMapperConfig

    @NotNull
    @PositiveOrZero
    @Schema(example = "5")
    private Integer quantidade;

    @Schema(example = "Sem cebola")
    private String observacao;
}
