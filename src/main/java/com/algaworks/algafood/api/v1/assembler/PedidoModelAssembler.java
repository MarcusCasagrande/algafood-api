package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.PedidoController;
import com.algaworks.algafood.api.v1.model.objectmodel.PedidoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public PedidoModelAssembler() {
        super(PedidoController.class, PedidoModel.class);
    }

    @Override
    public PedidoModel toModel(Pedido pedido) {
        PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);
        if (algaSecurity.autenticadoEEscopoLeitura()) {
            pedidoModel.add(algaLinks.linkToPedidos("pedidos"));
        }
        //pedidoModel.add(linkTo(PedidoController.class).withRel("pedidos")); // comentado pra ser trocado por template (linha acima), aula 19.18

        if (algaSecurity.podeGerenciarPedidos(pedido.getCodigo())) { // 23.39: verifica se user tem autorizacao pro link antes de enviar
            if (pedido.podeSerConfirmado())
                pedidoModel.add(algaLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
            if (pedido.podeSerEntregue())
                pedidoModel.add(algaLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
            if (pedido.podeSerCancelado())
                pedidoModel.add(algaLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
        }
        //pedidoModel.getRestaurante().add(linkTo(methodOn(RestauranteController.class).buscar(pedido.getRestaurante().getId())).withSelfRel());
        if (algaSecurity.autenticadoEEscopoLeitura()) {
            pedidoModel.getRestaurante().add(algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
            pedidoModel.getFormaPagamento().add(algaLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));
            pedidoModel.getEnderecoEntrega().getCidade().add(algaLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
            //pedidoModel.getItens().stream().forEach(itemPedido -> itemPedido.add(linkTo(methodOn(ProdutoController.class).buscar(pedidoModel.getRestaurante().getId(), itemPedido.getProdutoId())).withRel("produto")));
            pedidoModel.getItens().forEach(item -> item.add(algaLinks.linkToProduto(pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produtos")));
        }
        if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
            pedidoModel.getCliente().add(algaLinks.linkToUsuario(pedido.getCliente().getId()));
        }
        return pedidoModel;
    }

    @Override
    public CollectionModel<PedidoModel> toCollectionModel(Iterable<? extends Pedido> entities){
        return super.toCollectionModel(entities)
                .add(linkTo(PedidoController.class).withSelfRel());
    }

}
