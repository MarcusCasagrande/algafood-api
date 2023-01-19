package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteIdNomeModel;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteModel;
import com.algaworks.algafood.api.v1.openapi.model.RestauranteBasicoModelOpenApi;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


@Api(tags = "Restaurantes") // tag pra sair no SwaggerUI. Ver SpringFoxConfig.java
public interface RestauranteControllerOpenApi {

    @ApiImplicitParams({
            @ApiImplicitParam(value = "Nome da projeção de pedidos", allowableValues = "apenas-nome", // implicit de projecao que permite apenas o valor "apenas-nome". Aula 18.28
                    name = "projecao", paramType = "query", type = "string")
    })
    @ApiOperation(value = "Lista restaurantes", response = RestauranteBasicoModelOpenApi.class) // usa essa classe como resposta na documentacao
    public CollectionModel<RestauranteBasicoModel> listar();


    @ApiIgnore // (19.48): faz com que na sessao "schemas" (la embaixo) do swagger, esse tipo (CollectionModel«RestauranteIdNomeModel») nao apareca
    @ApiOperation(value = "Lista restaurantes", hidden = true) // hidden pois ja tem outro GET com o mesmo caminho. entao este fica oculto no Swagger (e sem o paramentro de projecao ali do JSONView) 18.28
    public CollectionModel<RestauranteIdNomeModel> listarApenasNomes();

    @ApiOperation(value = "Busca restaurante por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description  = "ID do restaurante inválido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public RestauranteModel buscar(@ApiParam(value = "Id de um restaurante", example = "1", required = true) Long id) throws Exception;

    @ApiOperation("Cadastra um restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description  = "Restaurante cadastrado")
    })
    public RestauranteModel adicionar(@ApiParam(name = "coorpor", value = "Representacao de um novo restaurante", required = true) RestauranteInput restauranteInput);

    @ApiOperation("Atualiza restaurante por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description  = "Restaurante atualizado"),
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public RestauranteModel atualizar(@ApiParam(value = "ID de um restaurante", example = "1", required = true) long id, @ApiParam(name = "corpoRes", value = "Representação de um novo restaurante", required = true) RestauranteInput restauranteInput);

    @ApiOperation("Ativar restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurante ativado com sucesso"),
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<Void> ativar(@ApiParam(value = "ID de um restaurante", example = "1", required = true) long id);

    @ApiOperation("Inativar restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurante inativado com sucesso"),
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<Void> inativar(@ApiParam(value = "ID de um restaurante", example = "1", required = true) long id);

    @ApiOperation("Fechar restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurante fechado com sucesso"),
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<Void> fechamento(@ApiParam(value = "ID de um restaurante", example = "1", required = true) long id);

    @ApiOperation("Abrir restaurante por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurante aberto com sucesso"),
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<Void> abertura(@ApiParam(value = "ID de um restaurante", example = "1", required = true) long id);

    @ApiOperation("Exclui restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description  = "Restaurante excluido"),
            @ApiResponse(responseCode = "404", description  = "Restaurante não encontrado", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
    })
    public void remover(@ApiParam(value = "ID de um restaurante", example = "1", required = true) long id);

    @ApiOperation("Ativa múltiplos restaurantes")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description  = "Restaurantes ativados com sucesso")
    })
    public void ativarMultiplos(@ApiParam(name = "corpo", value = "IDs de restaurantes", required = true) List<Long> restauranteIds);

    @ApiOperation("Inativa múltiplos restaurantes")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description  = "Restaurantes ativados com sucesso")
    })
    public void inativarMultiplos(@ApiParam(name = "corpo", value = "IDs de restaurantes", required = true) List<Long> restauranteIds);

}
