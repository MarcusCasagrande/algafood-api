package com.algaworks.algafood.api.v1.model.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EnderecoInput {

    @ApiModelProperty(example = "88820-000", required = true)
    @NotBlank
    private String cep;

    @ApiModelProperty(example = "Rua lalala", required = true)
    @NotBlank
    private String logradouro;

    @ApiModelProperty(example = "233", required = true)
    @NotBlank
    private String numero;

    @ApiModelProperty(example = "el complemiento")
    private String complemento;

    @ApiModelProperty(example = "Centro", required = true)
    @NotBlank
    private String bairro;

    @Valid
    @NotNull
    private CidadeIdInput cidade;

}
