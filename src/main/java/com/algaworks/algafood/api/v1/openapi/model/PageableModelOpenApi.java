package com.algaworks.algafood.api.v1.openapi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// classe criada para auxiliar na documentacao do SpringFox em métodos com pageable (como o listar() de CozinhaController). Ver aula 18.20
@Setter
@Getter
@ApiModel("Pageablee")
public class PageableModelOpenApi { //nome custom. sem obrigatoriedade

    // atributos de um Pageable (ver metodo listar() de cozinha no Postman). Mostra quais props da interface pageable serao mostradas (e documentadas) no SwaggerUI.

    @ApiModelProperty(example = "0", value = "Número da página (começa em 0)")
    private int page;

    @ApiModelProperty(example = "10", value = "Quantidade de elems por pagina")
    private int size;

    @ApiModelProperty(example = "nome,asc", value = "Nome da prop para ordenação")
    private List<String> sort;
}
