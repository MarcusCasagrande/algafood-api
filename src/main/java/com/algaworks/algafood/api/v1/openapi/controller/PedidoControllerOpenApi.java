package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.filter.PedidoFilter;
import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.PedidoModel;
import com.algaworks.algafood.api.v1.model.objectmodel.PedidoResumoModel;
import com.algaworks.algafood.core.springdoc.PageableParameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Pedidos", description = "Gerencia os pedidos dos users")
public interface PedidoControllerOpenApi {


    @PageableParameter
    @Operation(
            summary = "Pesquisa os pedidos", description = "Lista os pedidos pagináveis com base num conjunto de filtros",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "clienteId",
                            description = "ID do cliente para filtro da pesquisa",
                            example = "1", schema = @Schema(type = "integer")),
                    @Parameter(in = ParameterIn.QUERY, name = "restauranteId",
                            description = "ID do restaurante para filtro da pesquisa",
                            example = "1", schema = @Schema(type = "integer")),
                    @Parameter(in = ParameterIn.QUERY, name = "dataCriacaoInicio",
                            description = "Data/hora de criação inicial para filtro da pesquisa",
                            example = "2019-12-01T00:00:00Z", schema = @Schema(type = "string", format = "date-time")),
                    @Parameter(in = ParameterIn.QUERY, name = "dataCriacaoFim",
                            description = "Data/hora de criação final para filtro da pesquisa",
                            example = "2019-12-02T23:59:59Z", schema = @Schema(type = "string", format = "date-time"))
            }
    )
    public PagedModel<PedidoResumoModel> pesquisar(@Parameter(hidden = true) PedidoFilter filtro, @Parameter(hidden = true) Pageable pageable);


    @Operation(summary = "Busca um pedido por código", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do pedido inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public PedidoModel buscar(@Parameter(description = "UUID de um pedido", example = "18f8e7ba-a378-11ed-a8fc-0242ac120002", required = true) String codigoPedido);

    @Operation(summary = "Cadastra um grupo", responses = {
            @ApiResponse(responseCode = "201", description = "Pedido cadastrado")
    })
    public PedidoModel adicionar(@RequestBody(description = "Representa um pedido", required = true) PedidoInput pedidoInput);
}
