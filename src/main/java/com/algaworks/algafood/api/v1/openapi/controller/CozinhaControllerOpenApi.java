package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v1.model.objectmodel.CozinhaModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;


@Api(tags = "Cozinhaas")
public interface CozinhaControllerOpenApi<T, S> {

    @ApiOperation("Lista as cozinhas com paginação")
    public PagedModel<T> listar(Pageable pageable);

    @ApiOperation("Busca uma cozinha por ID")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "400", description  = "ID da cozinha inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Cozinha não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public T buscar(@ApiParam(value = "Id de uma cozinnha", example = "22", required = true) Long id);

    @ApiOperation("Cadastra uma cozinha")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description  = "Cozinha cadastrada") //response nao precisa definir content, pois o metodo ja define que é o CidadeModel
    })
    public T adicionar(@ApiParam(name = "corpoCoz", value = "Representacao de uma nova cozinha", required = true)S cozinhaInput);

    @ApiOperation("Atualiza cozinha pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description  = "Cozinha atualizada"),
            @ApiResponse(responseCode = "404", description  = "Cozinha não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public T atualizar(@ApiParam(value = "ID de uma cozinha", example = "1", required = true) long id, @ApiParam(name = "corpoCoz", value = "Representação de uma nova cozinha") S cozinhaInput);

    @ApiOperation("Exclui cozinha")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description  = "Cozinha excluida"),
            @ApiResponse(responseCode = "404", description  = "Cozinha não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public void remover(@ApiParam(value = "ID de uma cozinha", example = "1", required = true) Long id);
}
