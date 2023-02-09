package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.GenericModelAssembler;
import com.algaworks.algafood.api.v1.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.v1.model.filter.PedidoFilter;
import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.PedidoModel;
import com.algaworks.algafood.api.v1.model.objectmodel.PedidoResumoModel;
import com.algaworks.algafood.api.v1.openapi.controller.PedidoControllerOpenApi;
import com.algaworks.algafood.core.data.PageWrapper;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.domain.spec.PedidoSpecs;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EmissaoPedidoService emissaoPedidoService;

//    @Autowired
//    private GenericModelAssembler<Pedido, PedidoModel> GMAFull;

//    @Autowired
//    private GenericModelAssembler<Pedido, PedidoResumoModel> GMAResumo;

    @Autowired
    private GenericModelAssembler<Pedido, PedidoModel> GMAFull;

    @Autowired
    private PedidoModelAssembler PMA;

    @Autowired
    private PedidoResumoModelAssembler PRMA;

    @Autowired
    private GenericInputDisassembler<PedidoInput, Pedido> GID;

    @Autowired
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;

    @Autowired
    private AlgaSecurity algaSecurity;


    /**
        usar este metodo quando fizer uso do @JsonFilter (ver PedidoResumoModel)
     */
    /*
    @GetMapping
    public MappingJacksonValue listar(@RequestParam(required = false) String campos){
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<PedidoResumoModel> pedidosModel = GMAResumo.toCollectionModel(pedidos, PedidoResumoModel.class);
        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel); // envelopando
        SimpleFilterProvider filterProvider = new SimpleFilterProvider(); // criando simple filter
        // mandando usar o filtro de nome "pedidoFilter" (que es´ta declarado em PedidoResumoModel
        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());  // serializeAll: (serializa todas as propriedades da classe onde tem o filtro especificado) (diferentemente do JsonView, que poe filtros em cada prop)
        if (StringUtils.isNotBlank(campos)){ // se true, filtra os campos deixando apenas os definido no @RequiredParam
            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
        }
        pedidosWrapper.setFilters(filterProvider);
        return pedidosWrapper;
    }
     */

    /**
     * Utilizar esse método com o filtro do Squiggly
     * @return
     */
//    @GetMapping
//    public List<PedidoResumoModel> listar() {
//        List<Pedido> todosPedidos = pedidoRepository.findAll();
//        return GMAResumo.toCollectionModel(todosPedidos, PedidoResumoModel.class);
//    }

    @CheckSecurity.Pedidos.PodePesquisar
    @GetMapping
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, @PageableDefault(size = 10) Pageable pageable) { // nao precisa de RequestParam aqui, pois o Spring ja detecta aqui que é filtro
        pageable = traduzirPageable(pageable);
        Page<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageable);
        todosPedidos = new PageWrapper<>(todosPedidos, pageable); // <-- se no GETpedidos colocar uma prop mudada no sort (nomeRes, setado aqui), na hr de gerar links HATEOAS ele gera com nome da prop errado (a traduzida), aula 19.17
        PagedModel<PedidoResumoModel> pedidosPagedModelo = pagedResourcesAssembler.toModel(todosPedidos, PRMA); // usando resource assembler pra converter um Page em um PagedModel. Argumento CMA é para ser usado pra antes converter de Cozinha para Cozinha Model

        return pedidosPagedModelo;
    }

    // metodo page antigo acima sem hateoas
//    @GetMapping
//    public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable) { // nao precisa de RequestParam aqui, pois o Spring ja detecta aqui que é filtro
//        pageable = traduzirPageable(pageable);
//        Page<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageable);
//        List<PedidoResumoModel> pedidosResumoModel = GMAResumo.toCollectionModel(todosPedidos.getContent(), PedidoResumoModel.class);
//        Page<PedidoResumoModel> pageResumoModel = new PageImpl<>(pedidosResumoModel, pageable, todosPedidos.getTotalElements());
//        return pageResumoModel;
//    }

    private Pageable traduzirPageable(Pageable pageable){ // 13.11 +-
        var mapeamento = ImmutableMap.of(
                "codigo", "codigo",
                "nomeRes", "restaurante.nome", // <-- aula 19.17: prop mudada
               // "nomeCliente", "cliente.nome", // tudo isso por causa dessa linha

                "valorTotal", "valorTotal"
        );
        return PageableTranslator.translate(pageable, mapeamento);
    }

    @CheckSecurity.Pedidos.PodeBuscar
    @GetMapping(value = "/{codigoPedido}")
    public PedidoModel buscar(@PathVariable String codigoPedido){
        return PMA.toModel(emissaoPedidoService.buscarOuFalhar(codigoPedido));
    }

    @CheckSecurity.Pedidos.PodeCriar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@RequestBody @Valid PedidoInput pedidoInput){
        Pedido p = GID.toDomainModel(pedidoInput, Pedido.class);

        p.setCliente(new Usuario());
        // 23.17 com user de jwtToken daqui pra baixo
        p.getCliente().setId(algaSecurity.getUsuarioId());

        return PMA.toModel(emissaoPedidoService.emitir(p));
    }

//    @PutMapping("/{id}")
//    public CozinhaModel atualizar(@PathVariable long id, @RequestBody @Valid CozinhaInput cozinhaInput){
//        Cozinha c = cadastroCozinhaService.buscarOuFalhar(id);
//        //BeanUtils.copyProperties(cozinha, c, "id"); // util quando tiver uma caralhada de atributos. Terceiro atributo é de coisas a serem ignoradas
//        CID.copyToDomainObject(cozinhaInput, c);
//        return CMA.toModel(cadastroCozinhaService.salvar(c));
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void remover(@PathVariable Long id){
//        cadastroCozinhaService.excluir(id);
//    }
}
