package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.FotoProdutoModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Api(tags = "Produtos")
public interface RestauranteProdutoFotoControllerOpenApi {

    @ApiOperation(value = "Busca a foto do produto de um restaurante", produces = "application/json, image/jpeg, image/png")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description  = "ID do restaurante ou produto inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Foto de produto não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public FotoProdutoModel buscar(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId, @ApiParam(value = "Código do produto", example = "1", required = true) long produtoId);

    @ApiOperation(value = "Busca a foto do produto de um restaurante", hidden = true)
    public ResponseEntity<?> servirFoto(Long restauranteId, long produtoId, String acceptHeader) throws HttpMediaTypeNotAcceptableException;


    @ApiOperation(value = "Atualiza a foto do produto de um restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description  = "Foto do produto atualizada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Produto de restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public FotoProdutoModel atualizarFoto(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId, @ApiParam(value = "Código do produto", example = "1", required = true) long produtoId, @ApiParam(name = "corpo", value = "Representação de uma nova foto de produto") FotoProdutoInput fotoProdutoInput, MultipartFile arquivo) throws IOException;


    @ApiResponses({
            @ApiResponse(responseCode = "204", description  = "Foto do produto excluíd"),
            @ApiResponse(responseCode = "400", description  = "ID do restaurante ou produto inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Foto do produto nao encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public void deletarfoto(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId,  @ApiParam(value = "Código do produto", example = "1", required = true) long produtoId);
}
