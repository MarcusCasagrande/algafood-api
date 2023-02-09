package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.model.objectmodel.CozinhaModel;
import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@Controller
//@ResponseBody //  a resposta dos metodos desse controller devem ser enviadas como resposta da request
@Slf4j
@RestController // (já inclui @Controller e @ResponseBody)
//@RequestMapping(value = "/cozinhas", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@RequestMapping(value = "/v1/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController implements CozinhaControllerOpenApi<CozinhaModel, CozinhaInput> {

    //anotacao @Slf4j cria essa linha abaixo, com nome de "log"
    //private static final Logger logger = LoggerFactory.getLogger(CozinhaController.class);

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    private CozinhaModelAssembler CMA;

    @Autowired
    private CozinhaInputDisassembler CID;

    @Autowired
    private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

    @PreAuthorize("isAuthenticated()") // 23.21: define que metodo só será executado se cumprir a expressao (estar authenticado)
    @GetMapping //(produces = MediaType.APPLICATION_JSON_VALUE) //especifica que só aceita resposta em json
    public PagedModel<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable){ // o default é 20 se nao especificado. Com esse @PageableDefault dae seta pra 10
        // metodo nao retorna mais Page, e sim um PagedModel. (HATEOAS, aula 19.15)
        Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
        PagedModel<CozinhaModel> cozinhasPagedModelo = pagedResourcesAssembler.toModel(cozinhasPage, CMA); // usando resource assembler pra converter um Page em um PagedModel. Argumento CMA é para ser usado pra antes converter de Cozinha para Cozinha Model

        log.debug("testing DEBUG logger...");
        log.info("testing INFO logger...");
        log.warn("testing WARN logger...");
        log.error("testing ERROR logger...");
        return cozinhasPagedModelo;
    }

    // ANTIGO metodo listar com Page. Sem HATEOAS ainda:
//    @GetMapping //(produces = MediaType.APPLICATION_JSON_VALUE) //especifica que só aceita resposta em json
//    public Page<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable){ // o default é 20 se nao especificado. Com esse @PageableDefault dae seta pra 10
//        //usando agora Pageable
//        Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable); //findAll() ja tem um overload com pageable. Para outros métodos é só inserir um pageable e mudar o return (ver cozinha Rep)
//        List<CozinhaModel> cozinhasModel = CMA.toCollectionModel(cozinhasPage.getContent());
//        Page<CozinhaModel> cozinhasPageModel = new PageImpl<>(cozinhasModel, pageable, cozinhasPage.getTotalElements());
//        return cozinhasPageModel; //retorna lista de cozinhas que alem de paginadas, tem varias informacoes de metadata da pagina.
//    }

//    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
//    public CozinhasXmlWrapper listarXml(){
//        return new CozinhasXmlWrapper(cozinhaRepository.listar());
//    }

    //@ResponseStatus(HttpStatus.ALREADY_REPORTED)
    @CheckSecurity.Cozinhas.PodeConsultar // 23.23: self-made annotation
    @GetMapping(value = "/{cozinhaId}")
    public CozinhaModel buscar(@PathVariable("cozinhaId") Long id){
        return CMA.toModel(cadastroCozinhaService.buscarOuFalhar(id));

//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.LOCATION, "http://api.algafood.local:8080/cozinhas");
//        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities()); // ver authorities
        Cozinha c = CID.toDomainModel(cozinhaInput);
        return CMA.toModel(cadastroCozinhaService.salvar(c));
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @PutMapping("/{id}")
    public CozinhaModel atualizar(@PathVariable long id, @RequestBody @Valid CozinhaInput cozinhaInput){
        Cozinha c = cadastroCozinhaService.buscarOuFalhar(id);
        //BeanUtils.copyProperties(cozinha, c, "id"); // util quando tiver uma caralhada de atributos. Terceiro atributo é de coisas a serem ignoradas
        CID.copyToDomainObject(cozinhaInput, c);
        return CMA.toModel(cadastroCozinhaService.salvar(c));
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id){
        cadastroCozinhaService.excluir(id);
    }

}
