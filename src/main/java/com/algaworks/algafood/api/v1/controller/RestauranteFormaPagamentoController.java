package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.v1.model.objectmodel.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // (já inclui @Controller e @ResponseBody)
@RequestMapping(value = "/v1/restaurantes/{restauranteId}/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteFormaPagamentoController implements RestauranteFormaPagamentoControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    FormaPagamentoModelAssembler FPMA;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    @GetMapping
    @CheckSecurity.Restaurantes.PodeConsultar
    public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId){
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        CollectionModel<FormaPagamentoModel> formasPagamentoModel =  FPMA.toCollectionModel(restaurante.getFormasPagamento()).removeLinks();
        formasPagamentoModel.add(algaLinks.linkToRestauranteFormasPagamento(restauranteId)); // desafio 19.27
        if (algaSecurity.podeGerenciarFuncionamentoRestaurantes(restauranteId)) {
            formasPagamentoModel.add(algaLinks.linkToRestauranteFormaPagamentoAssociacao(restauranteId, "associar")); //(19.29)
            formasPagamentoModel.getContent().forEach(fpm -> { // para inserir link de desassociar formaPagamento (19.28)
                fpm.add(algaLinks.linkToRestauranteFormaPagamentoDesassociacao(restauranteId, fpm.getId(), "desassociar"));
            });
        }
        return formasPagamentoModel;
    }

    @PutMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId){
        cadastroRestauranteService.associarformaPagamento(restauranteId, formaPagamentoId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId){
        cadastroRestauranteService.desassociarformaPagamento(restauranteId, formaPagamentoId);
        return ResponseEntity.noContent().build();
    }

}
