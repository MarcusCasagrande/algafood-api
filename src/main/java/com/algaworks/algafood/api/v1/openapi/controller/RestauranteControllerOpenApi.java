package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteIdNomeModel;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteModel;
import com.algaworks.algafood.api.v1.openapi.model.RestauranteBasicoModelOpenApi;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurantes", description = "Gerencia os restaurantes")
public interface RestauranteControllerOpenApi {

    // 26.21: 2 metodos iguais, coloca um endpoint mostrando a projecao "apenas-nome"
    @Operation(summary = "Lista restaurantes", parameters = {
        @Parameter(name = "projecao", description = "Nome da projeção", example = "apenas-nome", in = ParameterIn.QUERY, required = false)
    })
    public CollectionModel<RestauranteBasicoModel> listar();

//    @ApiIgnore // (19.48): faz com que na sessao "schemas" (la embaixo) do swagger, esse tipo (CollectionModel«RestauranteIdNomeModel») nao apareca

    @Operation(hidden = true) // redudante nesse caso, acho
    public CollectionModel<RestauranteIdNomeModel> listarApenasNomes();

    @Operation(summary = "Busca um restaurante por ID", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do restaurante inválido", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) })
    })
    public RestauranteModel buscar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long id) throws Exception;

    @Operation(summary = "Cadastra um restaurante", responses = {
            @ApiResponse(responseCode = "201", description = "Restaurante cadastrado"),
    })
    public RestauranteModel adicionar(@RequestBody(description = "Representacao de um novo restaurante", required = true) RestauranteInput restauranteInput);

    @Operation(summary = "Atualiza um restaurante por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public RestauranteModel atualizar(@Parameter(description = "ID de um restaurante", example = "1", required = true) long id, @RequestBody(description = "Representacao do res a seer atualizado", required = true) RestauranteInput restauranteInput);

    @Operation(summary = "Ativa um restaurante por ID", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> ativar(@Parameter(description = "ID de um restaurante", example = "1", required = true) long id);

    @Operation(summary = "Desativa um restaurante por ID", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> inativar(@Parameter(description = "ID de um restaurante", example = "1", required = true) long id);

    @Operation(summary = "Atualizar restaurante", description = "Descricao updateRes...")
    public ResponseEntity<Void> fechamento(@Parameter(description = "ID de um restaurante", example = "1", required = true) long id);

    @Operation(summary = "Abre um restaurante por ID", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante aberto com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> abertura(@Parameter(description = "ID de um restaurante", example = "1", required = true) long id);

    @Operation(summary = "Remover restaurante", description = "Descricao updateRes...")
    public void remover(@Parameter(description = "ID de um restaurante", example = "1", required = true) long id);

    @Operation(summary = "Ativa múltiplos restaurantes", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurantes ativados com sucesso"),
    })
    public void ativarMultiplos(@RequestBody(description = "IDs de restaurantes", required = true) List<Long> restauranteIds);

    @Operation(summary = "Inativa múltiplos restaurantes", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurantes ativados com sucesso"),
    })
    public void inativarMultiplos(@RequestBody(description = "IDs de restaurantes", required = true) List<Long> restauranteIds);

}
