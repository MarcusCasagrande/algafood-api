package com.algaworks.algafood.api.v2.controller;


import com.algaworks.algafood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.algaworks.algafood.api.v2.assembler.CozinhaInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CozinhaModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
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
@RequestMapping(value = "/v2/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaControllerV2 implements CozinhaControllerOpenApi<CozinhaModelV2, CozinhaInputV2> {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    private CozinhaModelAssemblerV2 CMA;

    @Autowired
    private CozinhaInputDisassemblerV2 CID;

    @Autowired
    private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<CozinhaModelV2> listar(@PageableDefault(size = 10) Pageable pageable){

        Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
        PagedModel<CozinhaModelV2> cozinhasPagedModelo = pagedResourcesAssembler.toModel(cozinhasPage, CMA); // usando resource assembler pra converter um Page em um PagedModel. Argumento CMA é para ser usado pra antes converter de Cozinha para Cozinha Model
        return cozinhasPagedModelo;
    }

    @GetMapping(value = "/{cozinhaId}")
    public CozinhaModelV2 buscar(@PathVariable("cozinhaId") Long id){
        return CMA.toModel(cadastroCozinhaService.buscarOuFalhar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CozinhaModelV2 adicionar(@RequestBody @Valid CozinhaInputV2 cozinhaInput){
        Cozinha c = CID.toDomainModel(cozinhaInput);
        return CMA.toModel(cadastroCozinhaService.salvar(c));
    }

    @PutMapping("/{id}")
    public CozinhaModelV2 atualizar(@PathVariable long id, @RequestBody @Valid CozinhaInputV2 cozinhaInput){
        Cozinha c = cadastroCozinhaService.buscarOuFalhar(id);
        CID.copyToDomainObject(cozinhaInput, c);
        return CMA.toModel(cadastroCozinhaService.salvar(c));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id){
        cadastroCozinhaService.excluir(id);
    }

}
