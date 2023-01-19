package com.algaworks.algafood.api.v1.model.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RestauranteIdInput {

    @ApiModelProperty(example = "3", required = true)
    @NotNull
    private Long id;
}
