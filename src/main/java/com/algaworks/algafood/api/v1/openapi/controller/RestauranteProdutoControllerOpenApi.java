package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.ProdutoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.ProdutoModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;


@Api(tags = "Produtos")
public interface RestauranteProdutoControllerOpenApi {

    @ApiOperation("Lista os produtos de um restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description  = "ID do restaurante inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public CollectionModel<ProdutoModel> listar(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId, @ApiParam(value = "Indica se deve ou não incluir produtos inativos no resultado da listagem", example = "false", defaultValue = "false") Boolean incluirInativos);

    @ApiOperation("Cadastra uma cidade")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description  = "ID do restaurante ou produto inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Produto de restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ProdutoModel buscar(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId,@ApiParam(value = "Código do restaurante", example = "1", required = true)  Long produtoId);

    @ApiOperation("Cadastra uma cidade")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description  = "Produto cadastrado"),
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ProdutoModel adicionar(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId,  @ApiParam(name = "corpoPI", value = "Representação de um novo produto") ProdutoInput produtoInput);

    @ApiOperation("Atualiza um produto de um restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description  = "Produto atualizado"),
            @ApiResponse(responseCode = "404", description  = "Produto do restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ProdutoModel associar(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId, @ApiParam(value = "Código do restaurante", example = "1", required = true) Long produtoId, @ApiParam(name = "corpoPI", value = "Representação de um novo produto") ProdutoInput produtoInput);
}
