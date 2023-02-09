package com.algaworks.algafood.api.v1.openapi.controller;

import com.algaworks.algafood.api.v1.model.input.EstadoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.EstadoModel;
import com.algaworks.algafood.api.v1.model.objectmodel.PermissaoModel;
import com.algaworks.algafood.core.security.CheckSecurity;
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
import org.springframework.web.bind.annotation.GetMapping;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Permissões", description = "Lista as permissões")
public interface PermissaoControllerOpenApi {

    @Operation(summary = "Lista as permissões")
    public CollectionModel<PermissaoModel> listar();

}
