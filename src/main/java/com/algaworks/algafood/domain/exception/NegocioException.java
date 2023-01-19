package com.algaworks.algafood.domain.exception;

//"value" e "code" sao a mesma coisa
//@ResponseStatus(value = HttpStatus.BAD_REQUEST)//, reason = "Entidade não encontrada") // comentado, pois o handler trata isso
public class NegocioException extends RuntimeException {

    public NegocioException(String msg){
        super(msg);
    }

    // adicionar construtor com a CAUSA pro stack ficar mais completo no final
    public NegocioException(String msg, Throwable causa){
        super(msg, causa);
    }
}
