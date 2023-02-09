package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.objectmodel.UsuarioModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurantes")
public interface RestauranteUsuarioControllerOpenApi {

    @Operation(summary = "Lista Lista os usu[arios respons[aveis associados ao restaurante", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) })
    })
    public CollectionModel<UsuarioModel> listar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

    @Operation(summary = "Associação de restaurante com usuário responsável", responses = {
            @ApiResponse(responseCode = "204", description = "Associação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> adicionarResponsavel(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, @Parameter(description = "ID de um usuario", example = "1", required = true) Long usuarioId);

    @Operation(summary = "ADesassociação de restaurante com usuário responsável", responses = {
            @ApiResponse(responseCode = "204", description = "Associação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou usuário não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> removerResponsavel(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, @Parameter(description = "ID de um usuario", example = "1", required = true) Long usuarioId);
}
