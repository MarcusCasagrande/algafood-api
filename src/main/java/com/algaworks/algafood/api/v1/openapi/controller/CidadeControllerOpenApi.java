package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
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

//AULA 18.17
@SecurityRequirement(name = "security_auth") // 26.5
@Tag(name = "Cidades", description = "Gerencia as cidades") // 26.6
public interface CidadeControllerOpenApi<T, S> {

    @Operation(summary = "Lista as cidades") // 26.7
    public CollectionModel<T> listar();


    @Operation(summary = "Busca uma cidade por id", responses = { //26.12: status de respostas específicos por metodo
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(ref = "Problema"))), //26.14: Problema é o nome do schema definido na classe Problem.java
            @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
    })
//    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI (aula antiga, SpringFox)
//            @ApiResponse(responseCode = "400", description  = "ID da cidade inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
//            @ApiResponse(responseCode = "404", description  = "Cidade não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
//    })
    public T buscar(@Parameter(description = "ID de uma cidade", example = "1", required = true) Long cidadeId); //26.8: @Parameter

    @Operation(summary = "Cadastra uma cidade", description = "Cadastro de uma cidade, necessidade de um estado e nome válido")
    public T adicionar(@RequestBody(description = "Representacao de uma nova cidade", required = true) S S); // @RequestBody do io.swagger.v3

    @Operation(summary = "Atualiza uma cidade por id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public T atualizar(@Parameter(description = "ID de uma cidade", example = "1", required = true) long id,
                       @RequestBody(description = "Representacao de uma cidade com dados para atualizar", required = true) S S);

    @Operation(summary = "Exclui uma cidade por ID", responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void remover(@Parameter(description = "ID de uma cidade", example = "1", required = true) long id);

}
