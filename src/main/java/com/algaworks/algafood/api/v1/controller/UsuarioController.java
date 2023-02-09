package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.model.input.AtualizarSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.api.v1.model.objectmodel.UsuarioModel;
import com.algaworks.algafood.api.v1.openapi.controller.UsuarioControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE) // Explicita que a resposta é em JSON)
public class UsuarioController implements UsuarioControllerOpenApi {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @Autowired
    UsuarioModelAssembler UMA;

    @Autowired
    GenericInputDisassembler<UsuarioInput, Usuario> UID;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<UsuarioModel> listar(){
        return UMA.toCollectionModel(usuarioRepository.findAll());
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping(value = "/{usuarioId}")
    public UsuarioModel buscar(@PathVariable("usuarioId") Long id){
        return UMA.toModel(cadastroUsuarioService.buscarOuFalhar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioInput){
        Usuario u = UID.toDomainModel(usuarioInput, Usuario.class);
        return UMA.toModel(cadastroUsuarioService.salvar(u));
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarUsuario
    @PutMapping("/{id}")
    public UsuarioModel atualizar(@PathVariable long id, @RequestBody @Valid UsuarioInput usuarioInput){
        Usuario u = cadastroUsuarioService.buscarOuFalhar(id);
        UID.copyToDomainObject(usuarioInput, u);
        return UMA.toModel(cadastroUsuarioService.salvar(u));
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarPropriaSenha
    @PutMapping("/{id}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarSenha(@PathVariable long id, @RequestBody @Valid AtualizarSenhaInput atualizarSenhaInput){
        cadastroUsuarioService.alterarSenha(id, atualizarSenhaInput.getSenhaAtual(), atualizarSenhaInput.getNovaSenha());
    }
    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id){
        cadastroUsuarioService.excluir(id);
    }

}
