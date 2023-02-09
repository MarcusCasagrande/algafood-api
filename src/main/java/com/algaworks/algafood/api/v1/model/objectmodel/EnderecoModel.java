package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoModel {

    @Schema(example = "88820-000")
    private String cep;

    @Schema(example = "Rua lalala")
    private String logradouro;

    @Schema(example = "\"12\"")
    private String numero;

    @Schema(example = "ap-504")
    private String complemento;

    @Schema(example = "Centro")
    private String bairro;

    private CidadeResumoModel cidade;
}
