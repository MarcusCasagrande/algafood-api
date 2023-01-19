package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.objectmodel.FormaPagamentoModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@Api(tags = "Restaurantes")
public interface RestauranteFormaPagamentoControllerOpenApi {

    @ApiOperation("Lista formas de pagamento pelo ID do restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public CollectionModel<FormaPagamentoModel> listar(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId);

    @ApiOperation("Associação de restaurante com forma de pagamento")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "204", description  = "Associação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description  = "Restaurante ou forma de pagamento não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<Void> associar(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId, @ApiParam(value = "Código da formade pagamento", example = "1", required = true) Long formaPagamentoId);

    @ApiOperation("Desassociação de restaurante com forma de pagamento")
    @ApiResponses({ // adiciona esses codigos de retorno ao metodo no SwaggerUI
            @ApiResponse(responseCode = "204", description  = "Desassociação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description  = "Restaurante ou forma de pagamento não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<Void> desassociar(@ApiParam(value = "Código do restaurante", example = "1", required = true) Long restauranteId, @ApiParam(value = "Código da formade pagamento", example = "1", required = true) Long formaPagamentoId);
}
