package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.openapi.model.FormasPagamentoModelOpenApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

@Api(tags = "Formas de Pagto TAG")
public interface FormaPagamentoControllerOpenApi {

    @ApiOperation(value = "Lista as formas de pagamento") // comentado, pois nao funciona no SpringFox v3: response = FormasPagamentoModelOpenApi.class) // IMPORTANTE: (19.43) Como o método retorna ResponseEntity<Algo<outroAlgo>>, precisa incluir esse "response" ali pra dar certo no SpringFoxConfig
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = FormasPagamentoModelOpenApi.class)
    }) // utilizando anotacoes da v2 do SpringFox pois o "response" nao funciona na v3 (desafio 19.43)
    public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request);

    @ApiOperation("Busca uma forma de pagamento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description  = "ID da forma de pagamento inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "forma de pagamento não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<FormaPagamentoModel> buscar(@ApiParam(value = "Id de uma cozinnha", example = "22", required = true) Long id, ServletWebRequest request);

    @ApiOperation("Cadastra uma forma de pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description  = "Forma de pagto cadastrada")
    })
    public FormaPagamentoModel adicionar(@ApiParam(name = "corpoFP", value = "Representacao de uma nova forma de pagamento") FormaPagamentoInput formaPagamentoInput);

    @ApiOperation("Atualiza forma de pagto pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description  = "forma de pagto atualizada"),
            @ApiResponse(responseCode = "404", description  = "forma de pagto não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public FormaPagamentoModel atualizar(@ApiParam(value = "ID de uma forma de pagto", example = "1", required = true) long id, @ApiParam(name = "corpoFP", value = "Representacao de uma nova forma de pagamento") FormaPagamentoInput formaPagamentoInput);

    @ApiOperation("Exclui forma de pagto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description  = "forma de pagto excluida"),
            @ApiResponse(responseCode = "404", description  = "forma de pagto não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public void remover(@ApiParam(value = "ID de uma forma de pagto", example = "1", required = true) Long id);
}
