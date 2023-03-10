package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.openapi.model.FormasPagamentoModelOpenApi;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Forma de pagamento", description = "Gerencia as FPs")
public interface FormaPagamentoControllerOpenApi {

    @Operation(summary = "Lista as formas de pagamento")
    public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(@Parameter(hidden = true) ServletWebRequest request);


    @Operation(summary = "Busca uma forma de pagamento pelo ID", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID da forma de pagamento inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "FP não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<FormaPagamentoModel> buscar(@Parameter(description = "ID de uma forma de pagamento", example = "1", required = true) Long id, @Parameter(hidden = true) ServletWebRequest request);

    @Operation(summary = "Cadastra um grupo", responses = {
            @ApiResponse(responseCode = "201", description = "Forma de pagamento cadastrada")
    })
    public FormaPagamentoModel adicionar(@RequestBody(description = "Representacao de uma nova forma de pagamento", required = true) FormaPagamentoInput formaPagamentoInput);


    @Operation(summary = "Atualiza descricao de uma FP pelo ID", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID da FP inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "FP não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public FormaPagamentoModel atualizar(@Parameter(description = "ID de uma forma de pagamento", example = "1", required = true) long id, @RequestBody(description = "Representacao de uma nova forma de pagamento", required = true) FormaPagamentoInput formaPagamentoInput);

    @Operation(summary = "Exclui uma cidade por ID", responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "ID da FP inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "FP não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void remover(@Parameter(description = "ID de uma forma de pagamento", example = "1", required = true) Long id);
}
