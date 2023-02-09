package com.algaworks.algafood.api.v1.model.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.OffsetDateTime;

@Setter
@Getter
public class PedidoFilter { // value só vai nesses tipos que nao representam modelinput (pageagle, pedidofilter


    private Long clienteId;
    private Long restauranteId;
    @DateTimeFormat(iso = ISO.DATE_TIME) // Força o spring a reconhecer o String do frontEnd como sendo do dateformat requisitado
    private OffsetDateTime dataCriacaoInicio;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime dataCriacaoFim;
}
