package com.algaworks.algafood.domain.exception;

//"value" e "code" sao a mesma coisa
//@ResponseStatus(value = HttpStatus.NOT_FOUND)//, reason = "Entidade não encontrada") // comentado, pois o handler trata isso
public abstract class EntidadeNaoEncontradaException extends NegocioException {

    public EntidadeNaoEncontradaException(String msg){
        super(msg);
    }
}
