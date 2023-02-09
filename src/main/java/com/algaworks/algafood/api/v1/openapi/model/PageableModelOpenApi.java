package com.algaworks.algafood.api.v1.openapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// classe criada para auxiliar na documentacao do SpringFox em métodos com pageable (como o listar() de CozinhaController). Ver aula 18.20
@Setter
@Getter
public class PageableModelOpenApi { //nome custom. sem obrigatoriedade

    // atributos de um Pageable (ver metodo listar() de cozinha no Postman). Mostra quais props da interface pageable serao mostradas (e documentadas) no SwaggerUI.
    private int page;

    private int size;

    private List<String> sort;
}
