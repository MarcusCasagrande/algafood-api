package com.algaworks.algafood.domain.exception;

public class FotoNaoEncontradaException extends EntidadeNaoEncontradaException {

    public FotoNaoEncontradaException(String msg){
        super(msg);
    }

    public FotoNaoEncontradaException(Long restauranteId, Long produtoId){
        this(String.format("Não existe um cadastro de foto com código %d para o restaurante %d", produtoId, restauranteId));
    }
}
