package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.PedidoResumoModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@Setter
@Getter
public class PedidosModelOpenApi {// (19.45)

    private PedidoEmbeddedModelOpenApi _embedded;
    private Links _links;
    private PageModelOpenApi page;

    @Data
    public class PedidoEmbeddedModelOpenApi{
        private List<PedidoResumoModel> pedidosx;
    }
}
