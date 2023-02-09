package com.algaworks.algafood.api.v1.controller;


import com.algaworks.algafood.api.v1.assembler.EstadoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.EstadoModelAssembler;
import com.algaworks.algafood.api.v1.model.input.EstadoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.EstadoModel;
import com.algaworks.algafood.api.v1.openapi.controller.EstadoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController // (já inclui @Controller e @ResponseBody)
@RequestMapping("/v1/estados")
public class EstadoController implements EstadoControllerOpenApi {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @Autowired
    private EstadoModelAssembler EMA;

    @Autowired
    private EstadoInputDisassembler EID;

    @CheckSecurity.Estados.PodeConsultar
    @GetMapping
    public CollectionModel<EstadoModel> listar(){
        return EMA.toCollectionModel(estadoRepository.findAll());
    }

    @CheckSecurity.Estados.PodeConsultar
    @GetMapping("/{id}")
    public EstadoModel buscar(@PathVariable Long id){
        return EMA.toModel(cadastroEstadoService.buscarOuFalhar(id));
    }

    @CheckSecurity.Estados.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput){
        Estado e = EID.toDomainModel(estadoInput);
        return EMA.toModel(cadastroEstadoService.salvar(e));
    }

    @CheckSecurity.Estados.PodeEditar
    @PutMapping("/{id}")
    public EstadoModel atualizar(@PathVariable long id, @RequestBody @Valid EstadoInput estadoInput){
        Estado e = cadastroEstadoService.buscarOuFalhar(id);
        EID.copyToDomainObject(estadoInput, e);
        return EMA.toModel(cadastroEstadoService.salvar(e));
    }

    @CheckSecurity.Estados.PodeEditar
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable long id){
        cadastroEstadoService.excluir(id);
    }
}
