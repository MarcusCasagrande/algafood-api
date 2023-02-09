package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.FotoProdutoModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@SecurityRequirement(name = "security_auth")
@Tag(name = "Produtos")
public interface RestauranteProdutoFotoControllerOpenApi {

    //26.22: Colocar multiplos content-types no mesmo end-point (foto aceita json ou img e muda o retorno de acordo com o content-type
    @Operation(summary = "Busca a foto do produto de um restaurante", responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FotoProdutoModel.class)),
                    @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary")),
                    @Content(mediaType = "image/png", schema = @Schema(type = "string", format = "binary"))
            }),
            @ApiResponse(responseCode = "400", description = "ID do restaurante ou produto inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
            @ApiResponse(responseCode = "404", description = "Foto do produto não encontrada", content = {@Content(schema = @Schema(ref = "Problema")) })
    })
    public FotoProdutoModel buscar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, @Parameter(description = "ID de um produto", example = "1", required = true) long produtoId);

    @Operation(hidden = true)
    public ResponseEntity<?> servirFoto(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, @Parameter(description = "ID de um produto", example = "1", required = true) long produtoId, @Parameter(description = "Descricção dos tipos aceitáveis", example = "image/png", required = true) String acceptHeader) throws HttpMediaTypeNotAcceptableException;

    @Operation(summary = "Atualiza foto de produto do restaurante", responses = {
            @ApiResponse(responseCode = "200", description = "Foto do produto atualizada"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou produto nao encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
    })
    public FotoProdutoModel atualizarFoto(@Parameter(description = "ID do restaurante", example = "1", required = true) Long restauranteId,
                                          @Parameter(description = "ID do produto", example = "2", required = true) long produtoId,
                                          @RequestBody(description = "Representacao de dados de uma foto", required = true) FotoProdutoInput fotoProdutoInput) throws IOException;

    @Operation(summary = "Exclui a foto de um produto de um restaurante", responses = {
            @ApiResponse(responseCode = "204", description = "Foto do produto excluída"),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Foto do produto nao encontrada", content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void deletarfoto(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, @Parameter(description = "ID de um produto", example = "1", required = true) long produtoId);
}
