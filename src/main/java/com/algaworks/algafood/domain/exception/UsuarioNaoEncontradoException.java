package com.algaworks.algafood.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException {

    public UsuarioNaoEncontradoException(String msg){
        super(msg);
    }

    public UsuarioNaoEncontradoException(Long usuarioId){
        this(String.format("Não existe um cadastro de usuário com código %d", usuarioId));
    }
}
