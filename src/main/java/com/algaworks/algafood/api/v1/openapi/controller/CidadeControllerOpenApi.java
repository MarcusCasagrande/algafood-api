package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;

//AULA 18.17
@Api(tags = "Cidades") // tag pra sair no SwaggerUI. Ver SpringFoxConfig.java
public interface CidadeControllerOpenApi<T, S> {

    @ApiOperation("Lista as cidades")
    public CollectionModel<T> listar();

    @ApiOperation("Busca uma cidade por ID")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "400", description  = "ID da cidade inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Cidade não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public T buscar(@ApiParam(value = "Id de uma cidade", example = "211", required = true) Long id); // removido tambem o @PathVariable do id

    @ApiOperation("Cadastra uma cidade")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "201", description  = "Cidade cadastrada") //response nao precisa definir, pois o metodo ja define que é o T
    })
    public T adicionar(@ApiParam(name = "coorpo", value = "Representacao de uma nova cidade", required = true) S S);

    @ApiOperation("Atualiza cidade")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "200", description  = "Cidade atualizada"),
            @ApiResponse(responseCode = "404", description  = "Cidade não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public T atualizar(@ApiParam(value = "ID de uma cidade", example = "1", required = true) long id, @ApiParam(name = "corpoCit", value = "Representação de uma nova cidae") S S);

    @ApiOperation("Exclui cidade")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "204", description  = "Cidade excluida"),
            @ApiResponse(responseCode = "404", description  = "Cidade não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public void remover(@ApiParam(value = "ID de uma cidade", example = "1", required = true) long id);

}
