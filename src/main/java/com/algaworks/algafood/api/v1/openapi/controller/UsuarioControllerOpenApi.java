package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.AtualizarSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.api.v1.model.objectmodel.UsuarioModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;

@Api(tags = "Usuarios")
public interface UsuarioControllerOpenApi {


    @ApiOperation("Lista os usuarios")
    public CollectionModel<UsuarioModel> listar();

    @ApiResponses({
            @ApiResponse(responseCode = "400", description  = "ID do usuario inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "user não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    @ApiOperation("Buscar user by ID")
    public UsuarioModel buscar(@ApiParam(value = "Código do usuario", example = "1", required = true) Long id);

    @ApiResponses({
            @ApiResponse(responseCode = "201", description  = "Usuário cadastrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
    })
    @ApiOperation("Cadastra novo user")
    public UsuarioModel adicionar(UsuarioComSenhaInput usuarioInput);

    @ApiOperation("Atualiza user by ID")
    public UsuarioModel atualizar( long id, UsuarioInput usuarioInput);

    @ApiOperation("Atualiza a senha de um usuário")
    public void atualizarSenha(long id, AtualizarSenhaInput atualizarSenhaInput);


    @ApiOperation("Remover usuario (que aparenteente nao existe esse metodo nas aulas e eu pus aqui sei la pq)")
    public void remover(Long id);
}
