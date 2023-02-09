package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.model.objectmodel.UsuarioModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteUsuarioControllerOpenApi;
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
@RequestMapping(value = "/v1/restaurantes/{restauranteId}/responsaveis", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteUsuarioController implements RestauranteUsuarioControllerOpenApi {

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private UsuarioModelAssembler UMA;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    @GetMapping
    @CheckSecurity.Restaurantes.PodeConsultar
    public CollectionModel<UsuarioModel> listar(@PathVariable Long restauranteId){
        Restaurante r = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        var usuariosModel =  UMA.toCollectionModel(r.getUsuariosResponsaveis()).removeLinks();  // remove links pois é um links de geral pra usuarios, e nao pra "restaurante-usuario-responsavel (19.13)
        usuariosModel.add(algaLinks.linkToResponsaveisRestaurante(restauranteId));
        if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
            usuariosModel.add(algaLinks.linkToResponsavelRestauranteAssociacao(restauranteId, "associarResponsavel"));
            usuariosModel.getContent().forEach(um -> { // para inserir link de desassociar formaPagamento (19.28)
                um.add(algaLinks.linkToResponsavelRestauranteDesassociacao(restauranteId, um.getId(), "desassociarResponsavel"));
            });
        }
        return usuariosModel;
    }

    @PutMapping("/{usuarioId}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> adicionarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId){
        cadastroRestauranteService.associarResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{usuarioId}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removerResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId){
        cadastroRestauranteService.desassociarResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
