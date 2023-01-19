package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.filter.PedidoFilter;
import com.algaworks.algafood.api.v1.model.input.PedidoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.PedidoModel;
import com.algaworks.algafood.api.v1.model.objectmodel.PedidoResumoModel;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

@Api(tags = "Peedidos")
public interface PedidoControllerOpenApi {

    @ApiImplicitParams({ // indica que tem o param "campos" nesse metodo. Pois o Squilly trabalha com isso.
            @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
                    name = "campos", paramType = "query", type = "string") //paramtype = query é o que vem nos query da url
    })
    @ApiOperation("Lista todos os pedidos baseado nos filtros explicitqdos")
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable);


    @ApiImplicitParams({
            @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
                    name = "campos", paramType = "query", type = "string") //paramtype = query é o que vem nos query da url
    })
    @ApiResponses({
            @ApiResponse(responseCode = "404", description  = "Pedido não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    @ApiOperation("Buscar pedido por codigo UUID")
    public PedidoModel buscar(@ApiParam(example = "f9981ca4-5a5e-4da3-af04-933861df3e55 (UUID)", value = "Código do pedido", required = true) String codigoPedido);

    @ApiResponses({
            @ApiResponse(responseCode = "204", description  = "Cidade excluida")
    })
    @ApiOperation("Registra um pedido")

    public PedidoModel adicionar(@ApiParam(name = "PedidoInputt", value = "Representacao de um pedido", required = true) PedidoInput pedidoInput);
}
