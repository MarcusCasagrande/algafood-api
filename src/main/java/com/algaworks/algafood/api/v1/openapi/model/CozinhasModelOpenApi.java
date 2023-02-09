package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.CozinhaModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@Setter
@Getter
public class CozinhasModelOpenApi {// (19.41) PagedModel nao extende mais isso: extends PagedModelOpenApi<CozinhaModel> {

    private CozinhaEmbeddedModelOpenApi _embedded;
    private Links _links;
    private PageModelOpenApi page;

    @Data
    public class CozinhaEmbeddedModelOpenApi{
        private List<CozinhaModel> cozinhas;
    }
}
