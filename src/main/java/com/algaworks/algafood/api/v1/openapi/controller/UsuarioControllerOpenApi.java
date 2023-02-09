package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.AtualizarSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.api.v1.model.objectmodel.UsuarioModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Usuários", description = "Gerencia os usuários")
public interface UsuarioControllerOpenApi {

    @Operation(summary = "Lista os usuários", description = "Lista user description")
    public CollectionModel<UsuarioModel> listar();

    @Operation(summary = "Busca um usuário por ID", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do usuário inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
            @ApiResponse(responseCode = "404", description = "usuaripo não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public UsuarioModel buscar(@Parameter(description = "ID de um usuário", example = "1", required = true) Long id);

    @Operation(summary = "Cadastra um novo usuário", responses = {
            @ApiResponse(responseCode = "201")
    })
    public UsuarioModel adicionar(@RequestBody(description = "Representacao de um usuario com a senha", required = true) UsuarioComSenhaInput usuarioInput);

    @Operation(summary = "Atualiza usuário por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
            @ApiResponse(responseCode = "404", description = "usuaripo não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public UsuarioModel atualizar(@Parameter(description = "ID de um usuário", example = "1", required = true) long id, @RequestBody(description = "Representacao de um usuário com novos dados", required = true) UsuarioInput usuarioInput);

    @Operation(summary = "Atualiza senha de um usuário", responses = {
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "404", description = "usuaripo não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public void atualizarSenha(@Parameter(description = "ID de um usuário", example = "1", required = true) long id, @RequestBody(description = "Representacao de uma senha a ser atualizada", required = true) AtualizarSenhaInput atualizarSenhaInput);

    @Operation(summary = "Remove usuário", responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", description = "usuaripo não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public void remover(@Parameter(description = "ID de um usuário", example = "1", required = true) Long id);
}
