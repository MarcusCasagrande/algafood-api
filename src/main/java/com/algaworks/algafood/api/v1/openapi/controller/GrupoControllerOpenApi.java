package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.GrupoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.GrupoModel;
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
@Tag(name = "Grupos", description = "Gerencia os grupos de usuarios")
public interface GrupoControllerOpenApi {

    @Operation(summary = "Lista os grupos")
    public CollectionModel<GrupoModel> listar();

    @Operation(summary = "Busca um grupo por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do grupo inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public GrupoModel buscar(@Parameter(description = "ID de um grupo", example = "1", required = true) Long id);

    @Operation(summary = "Cadastra um grupo", description = "Cadastro de um grupo de usuário")
    public GrupoModel adicionar(@RequestBody(description = "Representacao de um novo grupo", required = true) GrupoInput grupoInput);


    @Operation(summary = "Atualiza um grupo por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do grupo inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public GrupoModel atualizar(@Parameter(description = "ID de um grupo", example = "1", required = true) long id, @RequestBody(description = "Representacao de um novo grupo", required = true) GrupoInput grupoInput);


    @Operation(summary = "Exclui uma cidade por ID", responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "ID do grupo inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void remover(@Parameter(description = "ID de um grupo", example = "1", required = true) Long id);

}
