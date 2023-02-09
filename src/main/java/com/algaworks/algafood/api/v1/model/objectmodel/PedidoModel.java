package com.algaworks.algafood.api.v1.model.objectmodel;

import com.algaworks.algafood.domain.model.StatusPedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Relation(collectionRelation = "LosPedidos")
@Getter
@Setter
public class PedidoModel extends RepresentationModel<PedidoModel> {

    @Schema(example = "UUID")
    private String codigo;

    @Schema(example = "40.00")
    private BigDecimal subtotal;

    @Schema(example = "1.50")
    private BigDecimal taxaFrete;

    @Schema(example = "41.50")
    private BigDecimal valorTotal;

    @Schema(example = "CRIADO")
    private StatusPedido status;

    @Schema(example = "2023-01-20T12:00:50.1234567890")
    private OffsetDateTime dataCriacao;

    @Schema(example = "2023-01-20T12:00:50.1234567890")
    private OffsetDateTime dataConfirmacao;

    @Schema(example = "2023-01-20T12:00:50.1234567890")
    private OffsetDateTime dataCancelamento;

    @Schema(example = "2023-01-20T12:00:50.1234567890")
    private OffsetDateTime dataEntrega;

    private FormaPagamentoModel formaPagamento;
    private RestauranteIdNomeModel restaurante;
    private UsuarioModel cliente;
    private EnderecoModel enderecoEntrega;
    private List<ItemPedidoModel> itens = new ArrayList<>();

}
