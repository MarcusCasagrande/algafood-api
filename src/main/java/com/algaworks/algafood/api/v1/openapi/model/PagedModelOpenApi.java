package com.algaworks.algafood.api.v1.openapi.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedModelOpenApi<T> { // CLASSE EXCLUIDA (19.45)

    // props que retornam no JSON de listar() de CozinhaController (com Pageable). Essa classe representa a documentação desse retorno. Aula 18.21
    private List<T> content;

    @ApiModelProperty(example = "10", value = "Quantidade de registros por página")
    private Long size;

    @ApiModelProperty(example = "50", value = "Total de registros")
    private Long totalElements;

    @ApiModelProperty(example = "5", value = "Total de páginas")
    private Long totalPages;

    @ApiModelProperty(example = "0", value = "Número da página (comeca com 0)")
    private Long number;
}
