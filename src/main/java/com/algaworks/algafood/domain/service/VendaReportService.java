package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.api.v1.model.filter.VendaDiariaFilter;

public interface VendaReportService {

    byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}
