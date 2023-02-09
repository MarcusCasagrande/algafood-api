package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.core.security.AlgaSecurity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class RootEntryPointController {

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    @Operation(hidden = true)
    @GetMapping
    public RootEntryPointModel root(){
        var rootEntryPointModel = new RootEntryPointModel();
        if (algaSecurity.autenticadoEEscopoLeitura()){
            rootEntryPointModel.add(algaLinks.linkToCozinhas("cozinhas"));
            rootEntryPointModel.add(algaLinks.linkToPedidos("pedidos"));
            rootEntryPointModel.add(algaLinks.linkToRestaurantes("restaurantes"));
            rootEntryPointModel.add(algaLinks.linkToFormasPagamento("formas-pagamento"));
            rootEntryPointModel.add(algaLinks.linkToEstados("estados"));
            rootEntryPointModel.add(algaLinks.linkToCidades("cidades"));
        }
        if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
            rootEntryPointModel.add(algaLinks.linkToGrupos("grupos"));
            rootEntryPointModel.add(algaLinks.linkToUsuarios("usuarios"));
            rootEntryPointModel.add(algaLinks.linkToPermissoes("permissoes"));
        }
        if (algaSecurity.podeConsultarEstatisticas()) {
            rootEntryPointModel.add(algaLinks.linkToEstatisticas("estatisticas"));
        }
        return rootEntryPointModel;
    }

    private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {}
}
