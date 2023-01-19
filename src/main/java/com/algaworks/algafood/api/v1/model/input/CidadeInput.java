package com.algaworks.algafood.api.v1.model.input;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(example = "Içara", required = true) // @ApiModelProperty tem em sua implementacao um "required() default  = false. Isso substitui o required da implementacao do bean @NotNull e por isso o spring fox nao coloca o "*" vermelho de required no SwaggerUI
    @NotBlank
    private String nome;

    @NotNull
    @Valid
    private EstadoIdInput estado;

}
