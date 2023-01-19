package com.algaworks.algafood.domain.listener;

import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoClientePedidoConfirmadoListener {

    @Autowired
    private  EnvioEmailService envioEmail;

    //@EventListener // <-- por padrão, executa ANTES do commit
    @TransactionalEventListener //<-- por padrao, executa APÓS o commit (como nao é crítico o email ser enviado, deixamos assim)
    //@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT) // <-- executa antes do commit, e se der pau, nao comita (estão bounded)
    public void aoConfirmarPedido(PedidoConfirmadoEvent event){
        //mandar email
        Pedido p = event.getPedido();
        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(p.getRestaurante().getNome() + " - Pedido confirmado")
                //.corpo("O pedido de código <strong>" + p.getCodigo() + "</strong> foi confirmado!")
                .corpo("emails/pedido-confirmado.html")
                .variavel("pedido", p)
                .destinatario(p.getCliente().getEmail())
                .build();
        envioEmail.enviar(mensagem);
    }
}
