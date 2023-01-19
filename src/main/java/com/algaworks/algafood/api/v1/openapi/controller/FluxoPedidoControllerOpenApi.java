package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

@Api(tags = "Peedidos")
public interface FluxoPedidoControllerOpenApi {

    @ApiOperation("onfirmacao de pedido")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "204", description  = "Pedido confirmado com sucesso"),
            @ApiResponse(responseCode = "404", description  = "Pedido não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<Void> confirmar(@ApiParam(value = "Código do pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", required = true) String codigoPedido);

    @ApiOperation("Registrar entrega de pedido")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "204", description  = "Entrega de pedido registrada com sucesso"),
            @ApiResponse(responseCode = "404", description  = "Pedido não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<Void> entregar(@ApiParam(value = "Código do pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", required = true) String codigoPedido);

    @ApiOperation("Cancelamento de pedido")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "204", description  = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description  = "Pedido não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<Void> cancelar(@ApiParam(value = "Código do pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", required = true) String codigoPedido);
}
