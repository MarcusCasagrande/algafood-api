package com.algaworks.algafood.api.v1.model.objectmodel;

import com.algaworks.algafood.domain.model.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Relation(collectionRelation = "LosPedidosResumo")
//@JsonFilter("pedidoFilter") // comentado agora, pois to usando squiggly nesta aula
@Getter
@Setter
public class PedidoResumoModel extends RepresentationModel<PedidoResumoModel> {

    @Schema(example = "um UUID ae")
    private String codigo;

    @Schema(example = "14.99")
    private BigDecimal subtotal;

    @Schema(example = "2.50")
    private BigDecimal taxaFrete;

    @Schema(example = "17.49")
    private BigDecimal valorTotal;

    @Schema(example = "Confirmado")
    private StatusPedido status;

    @Schema(example = "2023-01-20T12:00:50.1234567890")
    private OffsetDateTime dataCriacao;

    private RestauranteIdNomeModel restaurante;

    private UsuarioModel cliente;
}
