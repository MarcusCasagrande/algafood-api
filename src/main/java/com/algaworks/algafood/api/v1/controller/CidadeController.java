package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v1.model.input.CidadeInput;
import com.algaworks.algafood.api.v1.model.objectmodel.CidadeModel;
import com.algaworks.algafood.api.v1.openapi.controller.CidadeControllerOpenApi;
import com.algaworks.algafood.api.v1.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CidadeModelAssembler;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.core.web.AlgaMediaTypes;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController // (já inclui @Controller e @ResponseBody)
//@RequestMapping(value = "/cidades", produces = AlgaMediaTypes.V1_APPLICATION_JSON_VALUE)// produces pra versao (20.10)
@RequestMapping(value = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE) // voltando produces pro standard, mas com /v1 agora (20.13)
public class CidadeController implements CidadeControllerOpenApi<CidadeModel, CidadeInput> {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @Autowired
    private CidadeModelAssembler CMA;

    @Autowired
    private CidadeInputDisassembler CID;

    @CheckSecurity.Cidades.PodeConsultar
    @Deprecated
    @GetMapping // mudando retorno pra um tipo RepresentationModel, pros arrays de "links" no JSON de resposta. Aula 19.10
    public CollectionModel<CidadeModel> listar(){
        CollectionModel<CidadeModel> cidadesModel = CMA.toCollectionModel(cidadeRepository.findAll());

//        Feito agora pelo CollectionModel (aula 19. 11)
//        cidadesModel.forEach(cidadeModel -> {
//            Link link = linkTo(methodOn(CidadeController.class).buscar(cidadeModel.getId())).withSelfRel();
//            cidadeModel.add(link);
//            cidadeModel.add(linkTo(methodOn(CidadeController.class).listar()).withRel("cidades")); // modo 3
//            cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class).buscar(cidadeModel.getEstado().getId())).withSelfRel());
//        });
//        CollectionModel<CidadeModel> cidadesCollectionModel = CollectionModel.of(cidadesModel); // _embedded
//        cidadesCollectionModel.add(linkTo(CidadeController.class).withSelfRel()); //link ao final do JSON com /cidades
//        return cidadesCollectionModel;
        return cidadesModel;
    }

    @CheckSecurity.Cidades.PodeConsultar
    @GetMapping(value = "/{id}")
    public CidadeModel buscar(@PathVariable Long id){ //apiParam é coisa do Swagger
        CidadeModel cidadeModel =  CMA.toModel(cadastroCidadeService.buscarOuFalhar(id));
//        cidadeModel.add(Link.of("http://localhost:8080/cidades/1")); // padrao: "self"

        return cidadeModel;
    }

    @CheckSecurity.Cidades.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // @ApiParam(name = "coorpo", value = "Representacao de uma nova cidade") nao ta funcionando na versao 3 aparentemente
    public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput){
        try{
            Cidade c = CID.toDomainModel(cidadeInput);
            CidadeModel cidadeModel = CMA.toModel(cadastroCidadeService.salvar(c));
            // HATEOAS
            ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());
            return cidadeModel;
        } catch (EstadoNaoEncontradaException e){
            throw new NegocioException(e.getMessage()); // ok relancar com codigos HTTP distintos no controller, mas nao no service
        }
    }

    @CheckSecurity.Cidades.PodeEditar
    @PutMapping("/{id}")
    public CidadeModel atualizar(@PathVariable long id, @RequestBody @Valid CidadeInput cidadeInput){
        Cidade c = cadastroCidadeService.buscarOuFalhar(id);
        CID.copyToDomainObject(cidadeInput, c);
        try{
            return CMA.toModel(cadastroCidadeService.salvar(c));
        } catch (EstadoNaoEncontradaException e){
            // ok relancar com codigos HTTP distintos no controller, mas nao no service
            throw new NegocioException(e.getMessage(), e); // handler se mete quando tem esse "e" da causa aqui. Se tirar, nao pega e o NegocioException vai pro frontend sem passar pelo handler
        }
    }

    @CheckSecurity.Cidades.PodeEditar
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable long id){
        cadastroCidadeService.excluir(id);
    }

    /* //exception handler LOCAL
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> tratarNegocioException(NegocioException e){
        Problema problema = Problema.builder().dataHora(LocalDateTime.now()).msg(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problema);
    }
     */
}
