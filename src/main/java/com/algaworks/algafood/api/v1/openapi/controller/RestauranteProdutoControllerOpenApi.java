package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.ProdutoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.ProdutoModel;
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


@SecurityRequirement(name = "security_auth")
@Tag(name = "Produtos", description = "Gerencia os produtos")
public interface RestauranteProdutoControllerOpenApi {

    @Operation(summary = "Lista os produtos de um restaurante", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do restaurante inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public CollectionModel<ProdutoModel> listar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, @Parameter(description = "Incluir ou não produtos inativos", example = "false", required = true) Boolean incluirInativos);

    @Operation(summary = "Busca um produto de um restaurante", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do restaurante ou produto inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
            @ApiResponse(responseCode = "404", description = "Produto ou restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public ProdutoModel buscar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, @Parameter(description = "ID de um produto", example = "1", required = true) Long produtoId);

    @Operation(summary = "Cadastra um produto de um restaurante", responses = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })

    })
    public ProdutoModel adicionar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, @RequestBody(description = "Representacao de um novo produto", required = true) ProdutoInput produtoInput);

    @Operation(summary = "Atualiza um produto de um restaurante", responses = {
            @ApiResponse(responseCode = "200", description = "produto atualizado"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou produto não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
    })
    public ProdutoModel atualizar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, @Parameter(description = "ID de um produto", example = "1", required = true) Long produtoId, @RequestBody(description = "Representacao de um produto com dados atualizados", required = true) ProdutoInput produtoInput);
}
