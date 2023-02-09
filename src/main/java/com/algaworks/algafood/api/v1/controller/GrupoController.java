package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.v1.model.input.GrupoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.GrupoModel;
import com.algaworks.algafood.api.v1.openapi.controller.GrupoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController // (já inclui @Controller e @ResponseBody)
@RequestMapping(value = "/v1/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CadastroGrupoService cadastroGrupoService;

//    @Autowired
//    GenericModelAssembler<Grupo, GrupoModel> GMA;
    @Autowired
    GrupoModelAssembler GMA;

    @Autowired
    GenericInputDisassembler<GrupoInput, Grupo> GID;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<GrupoModel> listar(){
        return GMA.toCollectionModel(grupoRepository.findAll());
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping(value = "/{grupoId}")
    public GrupoModel buscar(@PathVariable("grupoId") Long id){
        return GMA.toModel(cadastroGrupoService.buscarOuFalhar(id));
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput){
        Grupo g = GID.toDomainModel(grupoInput, Grupo.class);
        return GMA.toModel(cadastroGrupoService.salvar(g));
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{id}")
    public GrupoModel atualizar(@PathVariable long id, @RequestBody @Valid GrupoInput grupoInput){
        Grupo g = cadastroGrupoService.buscarOuFalhar(id);
        GID.copyToDomainObject(grupoInput, g);
        return GMA.toModel(cadastroGrupoService.salvar(g));
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id){
        cadastroGrupoService.excluir(id);
    }

}
