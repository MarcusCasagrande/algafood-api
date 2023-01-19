package com.algaworks.algafood.domain.exception;

//"value" e "code" sao a mesma coisa
//@ResponseStatus(value = HttpStatus.NOT_FOUND) // Nao precisa deixar explicito, pois a classe pai ja é NOT_FOUND
public class PermissaoNaoEncontradaException extends EntidadeNaoEncontradaException {

    public PermissaoNaoEncontradaException(String msg){
        super(msg);
    }

    //opcional
    public PermissaoNaoEncontradaException(Long permissaoId){
        this(String.format("Não existe um cadastro de permissao com código %d", permissaoId));
    }
}
