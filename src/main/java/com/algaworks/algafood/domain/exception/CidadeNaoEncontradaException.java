package com.algaworks.algafood.domain.exception;

//"value" e "code" sao a mesma coisa
//@ResponseStatus(value = HttpStatus.NOT_FOUND) // Nao precisa deixar explicito, pois a classe pai ja é NOT_FOUND
public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {

    public CidadeNaoEncontradaException(String msg){
        super(msg);
    }

    //opcional
    public CidadeNaoEncontradaException(Long cidadeId){
        this(String.format("Não existe um cadastro de cidade com código %d", cidadeId));
    }
}
