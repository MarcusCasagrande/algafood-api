package com.algaworks.algafood.api.v2.controller;

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v1.openapi.controller.CidadeControllerOpenApi;
import com.algaworks.algafood.api.v2.assembler.CidadeInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CidadeModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
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
//@RequestMapping(value = "/cidades", produces = AlgaMediaTypes.V2_APPLICATION_JSON_VALUE)// produces pra versao (20.11)
@RequestMapping(value = "/v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE) // voltando produces pro standard, mas com /v2 agora (mediatype por URI) (20.13)
public class CidadeControllerV2 implements CidadeControllerOpenApi<CidadeModelV2, CidadeInputV2> {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @Autowired
    private CidadeModelAssemblerV2 CMA;

    @Autowired
    private CidadeInputDisassemblerV2 CID;

    @GetMapping
    public CollectionModel<CidadeModelV2> listar(){
        CollectionModel<CidadeModelV2> cidadesModel = CMA.toCollectionModel(cidadeRepository.findAll());
        return cidadesModel;
    }

    @GetMapping(value = "/{id}")
    public CidadeModelV2 buscar(@PathVariable Long id){
        CidadeModelV2 cidadeModel = CMA.toModel(cadastroCidadeService.buscarOuFalhar(id));
        return cidadeModel;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // @ApiParam(name = "coorpo", value = "Representacao de uma nova cidade") nao ta funcionando na versao 3 aparentemente
    public CidadeModelV2 adicionar(@RequestBody @Valid CidadeInputV2 cidadeInput){
        try{
            Cidade c = CID.toDomainModel(cidadeInput);
            CidadeModelV2 cidadeModel = CMA.toModel(cadastroCidadeService.salvar(c));
            // HATEOAS
            ResourceUriHelper.addUriInResponseHeader(cidadeModel.getIdCidade());
            return cidadeModel;
        } catch (EstadoNaoEncontradaException e){
            throw new NegocioException(e.getMessage()); // ok relancar com codigos HTTP distintos no controller, mas nao no service
        }
    }

    @PutMapping("/{id}")
    public CidadeModelV2 atualizar(@PathVariable long id, @RequestBody @Valid CidadeInputV2 cidadeInput){
        Cidade c = cadastroCidadeService.buscarOuFalhar(id);
        CID.copyToDomainObject(cidadeInput, c);
        try{
            return CMA.toModel(cadastroCidadeService.salvar(c));
        } catch (EstadoNaoEncontradaException e){
            throw new NegocioException(e.getMessage(), e);
        }
    }


    @DeleteMapping("/{id}") // comentado por problemas no Swagger (conteudo de apoio da 20.11)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable long id){
        cadastroCidadeService.excluir(id);
    }

}
