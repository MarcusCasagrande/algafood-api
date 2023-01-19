package com.algaworks.algafood.api.v1.openapi.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("Linkss")
public class LinksModelOpenApi {

    private LinkModel rel;

    @Getter
    @Setter
    @ApiModel("Linkk")
    private class LinkModel{

        private String href;
        private boolean templated;
    }

}
