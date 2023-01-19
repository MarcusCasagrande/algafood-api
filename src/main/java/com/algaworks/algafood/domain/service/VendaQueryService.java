package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.api.v1.model.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;

import java.util.List;

//interface de consulta de vendas (relatorio), nao de processo de negocio
public interface VendaQueryService {

    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}
