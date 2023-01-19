package com.algaworks.algafood.domain.exception;

//"value" e "code" sao a mesma coisa
//@ResponseStatus(value = HttpStatus.NOT_FOUND) // Nao precisa deixar explicito, pois a classe pai ja é NOT_FOUND
public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {
    public PedidoNaoEncontradoException(String codigoPedido){
        super(String.format("Não existe um cadastro de pedido com código %d", codigoPedido));
    }
}
