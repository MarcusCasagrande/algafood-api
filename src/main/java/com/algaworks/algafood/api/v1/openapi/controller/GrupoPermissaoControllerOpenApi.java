package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.objectmodel.PermissaoModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@Api(tags = "Grupos")
public interface GrupoPermissaoControllerOpenApi {

    @ApiResponses({
            @ApiResponse(responseCode = "400", description  = "ID do grupo inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "grupo não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    @ApiOperation("Lista as permissoes desse grupo pelo ID")
    public CollectionModel<PermissaoModel> listar(@ApiParam(value = "Código do grupo", example = "1", required = true) Long grupoId);

    @ApiResponses({
            @ApiResponse(responseCode = "204", description  = "Associação realizada com sucesso", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Grupo ou permissão não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    @ApiOperation("Associna nova permissao ao grupo")
    public ResponseEntity<Void> associar( @ApiParam(value = "Código do grupo", example = "1", required = true) Long grupoId,  @ApiParam(value = "Código da permissao", example = "1", required = true) Long permissaoId);


    @ApiResponses({
            @ApiResponse(responseCode = "204", description  = "Desassociação realizada com sucesso", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Grupo ou permissão não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    @ApiOperation("Desassocia nova permissao ao grupo")
    public ResponseEntity<Void> desassociar(@ApiParam(value = "Código do grupo", example = "1", required = true) Long grupoId, @ApiParam(value = "Código da permissao", example = "1", required = true) Long permissaoId);
}
