package com.algaworks.algafood.api.v1.model.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EstadoIdInput {

    @ApiModelProperty(example = "12", required = true) // @ApiModelProperty tem em sua implementacao um "required() default  = false. Isso substitui o required da implementacao do bean @NotNull e por isso o spring fox nao coloca o "*" vermelho de required no SwaggerUI
    @NotNull
    private Long id;
}
