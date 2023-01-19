package com.algaworks.algafood.api.v1.model.mixin;

import com.algaworks.algafood.domain.model.StatusPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public abstract class PedidoMixin {

    @JsonIgnore
    private OffsetDateTime dataCriacao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusPedido status;
}
