package com.algaworks.algafood.api.v1.model.filter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.OffsetDateTime;

@Setter
@Getter
public class PedidoFilter { // value só vai nesses tipos que nao representam modelinput (pageagle, pedidofilter

    @ApiModelProperty(example = "1", value = "ID do cliente para filtro da pesquisa")
    private Long clienteId;

    @ApiModelProperty(example = "1", value = "ID do restaurante para filtro da pesquisa")
    private Long restauranteId;

    @ApiModelProperty(example = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", value = "Data/hora de criação inicial para filtro da pesquisa")
    @DateTimeFormat(iso = ISO.DATE_TIME) // Força o spring a reconhecer o String do frontEnd como sendo do dateformat requisitado
    private OffsetDateTime dataCriacaoInicio;

    @ApiModelProperty(example = "2019-11-01T10:00:00Z", value = "Data/hora de criação final para filtro da pesquisa")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime dataCriacaoFim;
}
