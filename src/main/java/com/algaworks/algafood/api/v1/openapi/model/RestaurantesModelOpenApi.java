package com.algaworks.algafood.api.v1.openapi.model;

import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteBasicoModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@Setter
@Getter
public class RestaurantesModelOpenApi {// (19.47, desafio)

    private RestaurantesEmbeddedModelOpenApi _embedded;
    private Links _links;

    @Data
    public class RestaurantesEmbeddedModelOpenApi{
        private List<RestauranteBasicoModel> restaurantesModel;
    }
}
