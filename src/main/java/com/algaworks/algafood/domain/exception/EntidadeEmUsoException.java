package com.algaworks.algafood.domain.exception;

//@ResponseStatus(HttpStatus.CONFLICT) comentado, pois o handler trata isso
public class EntidadeEmUsoException extends NegocioException{

    public EntidadeEmUsoException(String msg){
        super(msg);
    }
}
