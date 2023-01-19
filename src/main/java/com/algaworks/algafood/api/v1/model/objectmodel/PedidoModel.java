package com.algaworks.algafood.api.v1.model.objectmodel;

import com.algaworks.algafood.domain.model.StatusPedido;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Relation(collectionRelation = "LosPedidos")
@ApiModel(value = "Um Pedido", description = "Representa informacoes de um pedido") // faz com que na documentacao, essa reprsentacoa mude de nome
@Getter
@Setter
public class PedidoModel extends RepresentationModel<PedidoModel> {

    @ApiModelProperty(example = "f9981ca4-5a5e-4da3-af04-933861df3e55")
    private String codigo;

    @ApiModelProperty(example = "35.2")
    private BigDecimal subtotal;

    @ApiModelProperty(example = "3.2", value = "frete do pedido")
    private BigDecimal taxaFrete;

    @ApiModelProperty(example = "38.4", value = "Valor dos itens + o frete")
    private BigDecimal valorTotal;

    @ApiModelProperty(example = "CRIADO")
    private StatusPedido status;

    @ApiModelProperty(example = "2019-12-01T20:34:04Z")
    private OffsetDateTime dataCriacao;

    @ApiModelProperty(example = "2019-12-01T20:34:04Z")
    private OffsetDateTime dataConfirmacao;

    @ApiModelProperty(example = "2019-12-01T20:34:04Z")
    private OffsetDateTime dataCancelamento;

    @ApiModelProperty(example = "2019-12-01T20:34:04Z")
    private OffsetDateTime dataEntrega;

    private FormaPagamentoModel formaPagamento;
    private RestauranteIdNomeModel restaurante;
    private UsuarioModel cliente;
    private EnderecoModel enderecoEntrega;
    private List<ItemPedidoModel> itens = new ArrayList<>();

}
