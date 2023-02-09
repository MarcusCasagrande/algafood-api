package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.v1.controller.EstatisticasController;
import com.algaworks.algafood.api.v1.model.filter.VendaDiariaFilter;
import com.algaworks.algafood.api.v1.model.input.AtualizarSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.input.UsuarioInput;
import com.algaworks.algafood.api.v1.model.objectmodel.FotoProdutoModel;
import com.algaworks.algafood.api.v1.model.objectmodel.UsuarioModel;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Estatísticas", description = "Estatísticas do Algafood")
public interface EstatisticasControllerOpenApi {

    @Operation(hidden = true)
    public EstatisticasController.EstatisticasModel estatisticas();


    @Operation(
            summary = "Consulta estatísticas de vendas diárias", description = "Desc de estatísticas",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "restauranteId",
                            description = "ID do restaurante para filtro da pesquisa",
                            example = "1", schema = @Schema(type = "integer")),
                    @Parameter(in = ParameterIn.QUERY, name = "dataCriacaoInicio",
                            description = "Data/hora de criação inicial para filtro da pesquisa",
                            example = "2019-12-01T00:00:00Z", schema = @Schema(type = "string", format = "date-time")),
                    @Parameter(in = ParameterIn.QUERY, name = "dataCriacaoFim",
                            description = "Data/hora de criação final para filtro da pesquisa",
                            example = "2019-12-02T23:59:59Z", schema = @Schema(type = "string", format = "date-time"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = VendaDiaria.class)),
                            @Content(mediaType = "image/pdf", schema = @Schema(type = "string", format = "binary"))
                    }),
//                    @ApiResponse(responseCode = "400", description = "ID do restaurante ", content = {@Content(schema = @Schema(ref = "Problema")) }),
//                    @ApiResponse(responseCode = "404", description = "Estatísticas não encontradas", content = {@Content(schema = @Schema(ref = "Problema")) })
            }
    )
    public List<VendaDiaria> consultarVendasDiarias(@Parameter(hidden = true) VendaDiariaFilter filter, @Parameter(description = "Offset time do local", schema = @Schema(type = "string", defaultValue = "+00:00")) String timeOffset);

    @Operation(hidden = true)
    public ResponseEntity<byte[]> consultarVendasDiariasPDF(VendaDiariaFilter filter, String timeOffset);
}
