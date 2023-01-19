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

@Relation(collectionRelation = "LosPedidosResumo")
//@JsonFilter("pedidoFilter") // comentado agora, pois to usando squiggly nesta aula
@ApiModel(value = "ResumoPedido", description = "Representa informacoes de um pedido") // faz com que na documentacao, essa reprsentacoa mude de nome
@Getter
@Setter
public class PedidoResumoModel extends RepresentationModel<PedidoResumoModel> {

    @ApiModelProperty(example = "um UUID qualquer")
    private String codigo;

    @ApiModelProperty(example = "35.2")
    private BigDecimal subtotal;

    @ApiModelProperty(example = "3.2")
    private BigDecimal taxaFrete;

    @ApiModelProperty(example = "38.4", value = "Valor dos itens + o frete")
    private BigDecimal valorTotal;

    @ApiModelProperty(example = "CRIADO")
    private StatusPedido status;

    @ApiModelProperty(example = "2019-12-01T20:34:04Z")
    private OffsetDateTime dataCriacao;

    private RestauranteIdNomeModel restaurante;

    private UsuarioModel cliente;
}
