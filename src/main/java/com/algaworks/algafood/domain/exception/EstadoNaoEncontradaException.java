package com.algaworks.algafood.domain.exception;

//"value" e "code" sao a mesma coisa
//@ResponseStatus(value = HttpStatus.NOT_FOUND) // Nao precisa deixar explicito, pois a classe pai ja é NOT_FOUND
public class EstadoNaoEncontradaException extends EntidadeNaoEncontradaException {

    public EstadoNaoEncontradaException(String msg){
        super(msg);
    }

    //opcional
    public EstadoNaoEncontradaException(Long estadoId){
        this(String.format("Não existe um cadastro de estado com código %d", estadoId));
    }
}
