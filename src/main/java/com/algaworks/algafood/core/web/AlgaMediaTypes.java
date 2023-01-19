package com.algaworks.algafood.core.web;

import org.springframework.http.MediaType;

public class AlgaMediaTypes { // classe utilitaria de tipos customizados de MediaType (20.10)

    // classe deletada na aula 20.13, pois acesso de versao via mediatype nao foi mais usado
    public static final String V1_APPLICATION_JSON_VALUE = "application/vnd.algafood.v1+json";
    public static final MediaType V1_APPLICATION_JSON = MediaType.valueOf(V1_APPLICATION_JSON_VALUE);

    public static final String V2_APPLICATION_JSON_VALUE = "application/vnd.algafood.v2+json";
    public static final MediaType V2_APPLICATION_JSON = MediaType.valueOf(V2_APPLICATION_JSON_VALUE);

}
