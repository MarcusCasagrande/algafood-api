package com.algaworks.algafood.domain.exception;

//"value" e "code" sao a mesma coisa
//@ResponseStatus(value = HttpStatus.NOT_FOUND) // Nao precisa deixar explicito, pois a classe pai ja é NOT_FOUND
public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException {

    public FormaPagamentoNaoEncontradaException(String msg){
        super(msg);
    }

    //opcional
    public FormaPagamentoNaoEncontradaException(Long formaPagamentoId){
        this(String.format("Não existe um cadastro de forma de pagamento com código %d", formaPagamentoId));
    }
}
