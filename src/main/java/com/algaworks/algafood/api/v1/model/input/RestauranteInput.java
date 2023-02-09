package com.algaworks.algafood.api.v1.model.input;

import com.algaworks.algafood.core.storage.validation.Multiplo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

//padrao de nome nao definido
@Getter
@Setter
public class RestauranteInput {


    // usar as anotacoes (javax.validation)  da classe do modelo (Restaurante)
    @NotBlank
    @Schema(example = "tuc-tuc res")
    private String nome;

    @NotNull
    @Multiplo(numero = 5)
    @Schema(example = "2.50", required = true)
    private BigDecimal taxaFrete;


    @NotNull
    @Valid
    private CozinhaIdInput cozinha;

    @Valid
    @NotNull
    private EnderecoInput endereco;
}
