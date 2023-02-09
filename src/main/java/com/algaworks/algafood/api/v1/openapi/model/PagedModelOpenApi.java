package com.algaworks.algafood.api.v1.openapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedModelOpenApi<T> { // CLASSE EXCLUIDA (19.45)

    // props que retornam no JSON de listar() de CozinhaController (com Pageable). Essa classe representa a documentação desse retorno. Aula 18.21
    private List<T> content;

    private Long size;

    private Long totalElements;

    private Long totalPages;

    private Long number;
}
