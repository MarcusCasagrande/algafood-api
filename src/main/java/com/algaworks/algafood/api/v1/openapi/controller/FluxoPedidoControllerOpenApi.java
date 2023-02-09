package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Pedidos", description = "Gerencia os pedidos dos users")
public interface FluxoPedidoControllerOpenApi {

    @Operation(summary = "Confirmação do pedido", responses = {
            @ApiResponse(responseCode = "204", description = "Pedido confirmado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public ResponseEntity<Void> confirmar(@Parameter(description = "UUID do pedido", example = "(um UUID qualquer))", required = true) String codigoPedido);

    @Operation(summary = "Registra entrtega do pedido", responses = {
            @ApiResponse(responseCode = "204", description = "Pedido entregue com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public ResponseEntity<Void> entregar(@Parameter(description = "UUID do pedido", example = "(um UUID qualquer))", required = true) String codigoPedido);

    @Operation(summary = "Registra cancelamento do poedido", responses = {
            @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public ResponseEntity<Void> cancelar(@Parameter(description = "UUID do pedido", example = "(um UUID qualquer))", required = true) String codigoPedido);
}
