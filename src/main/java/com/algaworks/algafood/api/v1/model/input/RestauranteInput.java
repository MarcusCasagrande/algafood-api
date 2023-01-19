package com.algaworks.algafood.api.v1.model.input;

import com.algaworks.algafood.core.validation.Multiplo;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(example = "Thay", required = true)
    // usar as anotacoes (javax.validation)  da classe do modelo (Restaurante)
    @NotBlank
    private String nome;

    @ApiModelProperty(example = "3.20", required = true)
    @NotNull
    @Multiplo(numero = 5)
    private BigDecimal taxaFrete;


    @NotNull
    @Valid
    private CozinhaIdInput cozinha;

    @Valid
    @NotNull
    private EnderecoInput endereco;
}
