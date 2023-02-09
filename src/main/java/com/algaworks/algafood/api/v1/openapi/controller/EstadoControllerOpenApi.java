package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.v1.model.input.EstadoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.EstadoModel;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.core.springdoc.PageableParameter;
import com.algaworks.algafood.domain.model.Estado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Estados", description = "Gerencia os estados")
public interface EstadoControllerOpenApi {

    @Operation(summary = "Lista Estados", description = "Estsado description")
    public CollectionModel<EstadoModel> listar();

    @Operation(summary = "Busca um estado por ID", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do estado inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public EstadoModel buscar(@Parameter(description = "ID de um estado", example = "1", required = true) Long id);

    @Operation(summary = "Cadastra um estado", responses = {
            @ApiResponse(responseCode = "201", description = "Estado cadastrado")
    })
    public EstadoModel adicionar(@RequestBody(description = "Representacao de um novo estado", required = true) EstadoInput estadoInput);

    @Operation(summary = "Atualiza um estado por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Estado atualizado"),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) })
    })
    public EstadoModel atualizar(@Parameter(description = "ID de um estado", example = "1", required = true) long id, @RequestBody(description = "Representacao de um novo estado", required = true) EstadoInput estadoInput);

    @Operation(summary = "Exclui um estado por ID", responses = {
            @ApiResponse(responseCode = "204", description = "Estado excluído"),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) })
    })
    public void remover(@Parameter(description = "ID de um estado", example = "1", required = true) long id);
}
