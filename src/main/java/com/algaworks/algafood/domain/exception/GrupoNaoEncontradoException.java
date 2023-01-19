package com.algaworks.algafood.domain.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException {

    public GrupoNaoEncontradoException(String msg){
        super(msg);
    }

    public GrupoNaoEncontradoException(Long grupoId){
        this(String.format("Não existe um cadastro de grupo com código %d", grupoId));
    }
}
