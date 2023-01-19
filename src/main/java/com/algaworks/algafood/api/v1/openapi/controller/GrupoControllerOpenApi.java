package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.GrupoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.GrupoModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;

@Api(tags = "Grupos") // tag pra sair no SwaggerUI. Ver SpringFoxConfig.java
public interface GrupoControllerOpenApi {

    @ApiOperation("Lista os grupos disponíveis")
    public CollectionModel<GrupoModel> listar();

    @ApiOperation("Busca um grupoz por ID")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "400", description  = "ID do grupow inválids", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Grupo nãoa encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public GrupoModel buscar(@ApiParam(value = "Id de um grrupo", example = "2", required = true) Long id);

    @ApiOperation("Cadastra um grupo de usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description  = "Grupo cadastrado") //response nao precisa definir, pois o metodo ja define que é o CidadeModel
    })
    public GrupoModel adicionar(@ApiParam(name = "coorpo", value = "Representacao de um novo grupo de userss") GrupoInput grupoInput);


    @ApiOperation("Atualiza grupo por ID")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "200", description  = "Grupo atualizado"),
            @ApiResponse(responseCode = "404", description  = "Grupo não encontradow", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public GrupoModel atualizar(@ApiParam(value = "ID de um grupo", example = "1", required = true) long id, @ApiParam(name = "corpo", value = "Representação de um grupo com os novos dados") GrupoInput grupoInput);

    @ApiOperation("Exclui um grupo por ID")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "204", description  = "Grupo excluídoe"),
            @ApiResponse(responseCode = "404", description  = "Grupoo não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public void remover(@ApiParam(value = "ID de um grupo", example = "1", required = true) Long id);

}
