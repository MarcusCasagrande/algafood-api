package com.algaworks.algafood.api.v2.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter

public class CidadeInputV2 {

    // usar as anotacoes (javax.validation)  da classe do modelo (Restaurante)

    //@ApiModelProperty(example = "Içara", required = true) // spirngfox, diz no SwaggerUi que é obrigatorio
    @NotBlank
    private String nomeCidade;

    @NotNull
    private Long idEstado;

//    @NotNull
//    @Valid
//    private EstadoIdInput estado;

}
