package com.algaworks.algafood.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoInput {

    @Schema(example = "Batata frita", required = true)
    @NotBlank
    private String nome;

    @Schema(example = "Batata fria no oleo", required = true)
    @NotBlank
    private String descricao;

    @Schema(example = "12.50", required = true)
    @NotNull
    @PositiveOrZero
    private BigDecimal preco;

    @Schema(example = "true", required = true)
    @NotNull
    private Boolean ativo;
}
