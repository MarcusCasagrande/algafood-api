package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.input.ProdutoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.ProdutoModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.algaworks.algafood.api.v1.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/restaurantes/{restauranteId}/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private CadastroProdutoService cadastroProdutoService;

//    @Autowired
//    private GenericModelAssembler<Produto, ProdutoModel> PMA;

    @Autowired
    private ProdutoModelAssembler PMA;

    @Autowired
    private GenericInputDisassembler<ProdutoInput, Produto> PID;

    @Autowired
    private AlgaLinks algaLinks;

    @GetMapping
    @CheckSecurity.Restaurantes.PodeConsultar
    public CollectionModel<ProdutoModel> listar(@PathVariable Long restauranteId, @RequestParam(required = false) Boolean incluirInativos){
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        List<Produto> todosProdutos = null;
        if (incluirInativos){
            todosProdutos = produtoRepository.findByRestaurante(restaurante);
        } else {
            todosProdutos = produtoRepository.findAtivosByRestaurante(restaurante);
        }
        return PMA.toCollectionModel(todosProdutos)
                .add(algaLinks.linkToProdutos(restauranteId));
    }

    @GetMapping("/{produtoId}")
    @CheckSecurity.Restaurantes.PodeConsultar
    public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId){
        return PMA.toModel(cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    public ProdutoModel adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInput produtoInput){
        Restaurante r = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        Produto p = PID.toDomainModel(produtoInput, Produto.class);
        p.setRestaurante(r);
        return PMA.toModel(cadastroProdutoService.salvar(p));
    }

    @PutMapping("/{produtoId}")
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    public ProdutoModel associar(@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoInput produtoInput){
        Produto p = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
        PID.copyToDomainObject(produtoInput, p);
        return PMA.toModel(cadastroProdutoService.salvar(p));
    }

//    @DeleteMapping("/{produtoId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void desassociar(@PathVariable Long restauranteId, @PathVariable Long produtoId){
//        cadastroProdutoService.desassociarProduto(restauranteId, produtoId);
//    }

}
