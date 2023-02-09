package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.objectmodel.GrupoModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Usuários")
public interface UsuarioGrupoControllerOpenApi {

    @Operation(summary = "Lista os grupos associados a um usuário", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do usuário inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public CollectionModel<GrupoModel> listar(@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId);

    @Operation(summary = "Associação de grupo com usuário", responses = {
            @ApiResponse(responseCode = "204", description = "Associação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "usuário ou grupo não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> associar(@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId, @Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId);

    @Operation(summary = "Desassociação de grupo com usuário", responses = {
            @ApiResponse(responseCode = "204", description = "Associação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "usuário ou grupo não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> desassociar(@Parameter(description = "ID de um usuário", example = "1", required = true) Long usuarioId, @Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId);

}
