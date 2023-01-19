package com.algaworks.algafood.domain.exception;

//"value" e "code" sao a mesma coisa
//@ResponseStatus(value = HttpStatus.NOT_FOUND) // Nao precisa deixar explicito, pois a classe pai ja é NOT_FOUND
public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {

    public RestauranteNaoEncontradoException(String msg){
        super(msg);
    }

    //opcional
    public RestauranteNaoEncontradoException(Long restauranteId){
        this(String.format("Não existe um cadastro de restaurante com código %d", restauranteId));
    }
}
