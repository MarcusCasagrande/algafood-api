package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.core.springdoc.PageableParameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cozinhas", description = "Gerencia as cozinhas")
public interface CozinhaControllerOpenApi<T, S> {


    @PageableParameter
    @Operation(summary = "Lista as cozinhas com paginação")
    public PagedModel<T> listar(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Busca um cozinha por ID", description = "Cadastro de um grupo de usuário")
    public T buscar(@Parameter(description = "ID de uma cozinha", example = "1", required = true) Long id);

    @Operation(summary = "Cadastra uma cozinha", description = "Cadastro de uma cozinha nova")
    public T adicionar(@RequestBody(description = "Representacao de uma nova cozinha", required = true) S cozinhaInput);

    @Operation(summary = "Atualiza uma cozinha por ID", description = "Atualiza Cozinha por ID de novo")
    public T atualizar(@Parameter(description = "ID de uma cozinha", example = "1", required = true) long id, @RequestBody(description = "Representacao de uma nova cozinha", required = true) S cozinhaInput);

    @Operation(summary = "Remove uma cozinha por ID", description = "É o que diz no sumario, bicho")
    public void remover(@Parameter(description = "ID de uma cozinha", example = "1", required = true) Long id);
}
