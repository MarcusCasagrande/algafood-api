package com.algaworks.algafood.api.v1.model.input;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CidadeInput {

    // usar as anotacoes (javax.validation)  da classe do modelo (Restaurante)

    //@ApiModelProperty(example = "Içara", required = true) // spirngfox, diz no SwaggerUi que é obrigatorio
    @NotBlank
    @Schema(example = "Içara", required = true) //26.10: caso nao tenha @NotBlank/@NotNull, o required serve
    private String nome;

    @NotNull
    @Valid
    private EstadoIdInput estado;

}
