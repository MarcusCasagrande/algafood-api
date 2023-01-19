package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.filter.VendaDiariaFilter;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/estatisticas")
public class EstatisticasController
{

    @Autowired
    private VendaQueryService vendaQueryService;

    @Autowired
    private VendaReportService vendaReportService;

    @Autowired
    private AlgaLinks algaLinks;

    @CheckSecurity.Estatisticas.PodeConsultar
    @GetMapping
    public EstatisticasModel estatisticas(){
        EstatisticasModel estatisticasModel = new EstatisticasModel();
        estatisticasModel.add(algaLinks.linkToEstatisticasVendasDiarias("vendas-diarias"));
        return estatisticasModel;
    }

    @CheckSecurity.Estatisticas.PodeConsultar
    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE) // path e value é a mesma coisa
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filter,
                             @RequestParam(required = false, defaultValue = "+00:00") String timeOffset){
        return vendaQueryService.consultarVendasDiarias(filter, timeOffset);
    }

    @CheckSecurity.Estatisticas.PodeConsultar
    @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consultarVendasDiariasPDF(VendaDiariaFilter filter,
                             @RequestParam(required = false, defaultValue = "+00:00") String timeOffset){
        byte[] bytesPdf = vendaReportService.emitirVendasDiarias(filter, timeOffset);
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf"); // attachment: indica que o conteudo deve ser baixado pelo cliente, e nao exibido inline. filename: indica o nome sugerido do file ao ser baixado
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(bytesPdf);
    }

    public static class EstatisticasModel extends RepresentationModel<EstatisticasModel> {}
}
