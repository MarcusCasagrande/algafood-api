package com.algaworks.algafood.domain.exception;

//"value" e "code" sao a mesma coisa
//@ResponseStatus(value = HttpStatus.NOT_FOUND) // Nao precisa deixar explicito, pois a classe pai ja é NOT_FOUND
public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

    public CozinhaNaoEncontradaException(String msg){
        super(msg);
    }

    //opcional
    public CozinhaNaoEncontradaException(Long cozinhaId){
        this(String.format("Não existe um cadastro de cozinha com código %d", cozinhaId));
    }
}
