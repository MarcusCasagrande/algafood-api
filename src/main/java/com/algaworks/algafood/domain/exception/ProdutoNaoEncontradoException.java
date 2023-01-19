package com.algaworks.algafood.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {

    public ProdutoNaoEncontradoException(String msg){
        super(msg);
    }

    //opcional
    public ProdutoNaoEncontradoException(Long produtoId){
        this(String.format("Não existe um cadastro de produto com código %d", produtoId));
    }

    public ProdutoNaoEncontradoException(Long restauranteId, Long produtoId) {
        this(String.format("Não existe um cadastro de produto com código %d para o restaurante de código %d",
                produtoId, restauranteId));
    }
}
