package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {

    public static final String MSG_STATUS_NAO_ALTERADO = "Status do pedido %d não pode ser alterado de %s para %s";
    @Autowired
    private EmissaoPedidoService emissaoPedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public void confirmar(String codigoPedido){
        Pedido p = emissaoPedidoService.buscarOuFalhar(codigoPedido);
        p.confirmar();
        // particularidade do registerEvent: apesar de ser um metodo @Transactional, para que o evento seja disparado, é necessario explicitar o save de um repository do spring data
        pedidoRepository.save(p);
    }

    @Transactional
    public void entregar(String codigoPedido){
        Pedido p = emissaoPedidoService.buscarOuFalhar(codigoPedido);
        p.entregar();
        // ta dentro do Transactional, ja faz o commit mesmo sem fazer .save()
    }

    @Transactional
    public void cancelar(String codigoPedido){
        Pedido p = emissaoPedidoService.buscarOuFalhar(codigoPedido);
        p.cancelar();
        // particularidade do registerEvent: apesar de ser um metodo @Transactional, para que o evento seja disparado, é necessario explicitar o save de um repository do spring data
        pedidoRepository.save(p);
    }
}
