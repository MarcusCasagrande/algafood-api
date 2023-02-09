package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.CozinhaModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteBasicoModelOpenApi { // aula 18.28: classe criada apenas pra documentacao: RestauranteController.listar(), nao retornatodas as propriedades de RestauranteModel, gracas ao JsonView do metodo. Entao esta classe resume (corta) algumas props que iriam aparecer na documentacao, mas que gracas ao json view, nunca viriam por aquele metodo

    private Long id;

    private String nome;

    private BigDecimal taxaFrete; // antes usado "precoFrete" pro modelMapper. Mas foi tirado.

    private CozinhaModel cozinha;
}
