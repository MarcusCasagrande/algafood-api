package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.EstadoModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@Setter
@Getter
public class EstadosModelOpenApi { // (19.42)

    private EstadoEmbeddedModelOpenApi _embedded;
    private Links _links;

    @Data
    public class EstadoEmbeddedModelOpenApi{
        private List<EstadoModel> estados;
    }
}
