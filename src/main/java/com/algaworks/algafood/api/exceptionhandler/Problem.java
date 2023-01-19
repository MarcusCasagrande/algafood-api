package com.algaworks.algafood.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@ApiModel("Problema") //  faz com que o modelo tenha esse nome no SwaggerUi
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder // nao tem construtores. Pra instanciar tem que fazer Problema.builder().<nome do campo>(<valor>)
public class Problem {

    @ApiModelProperty(example = "400", position = 1) // define a posicao (ordem) que as prop aparecem no SwaggerUI
    private Integer status;

    @ApiModelProperty(example = "2022-10-17T04:29:20.405183Z")
    private OffsetDateTime timestamp;

    @ApiModelProperty(example = "https://algafood.com.br/dados-invalidos", position = 5)
    private String type;

    @ApiModelProperty(example = "Dados inválidos", value = "valor teste")
    private String title;

    @ApiModelProperty(example = "Um ou mais campos estão inválidos.")
    private String detail;

    @ApiModelProperty(example = "Um ou mais campos estão inválidos.")
    private String userMessage; // <-- extensao, nao é padrao da especificacao. Mensagem para ajudar a informar o usuario final

    @ApiModelProperty(example = "Lista de objetos ou caompos que geraram o erro (opcional)")
    private List<Objects> objects;

    @ApiModel("ObjetoProblema") // nomeia o Schema pro SwaggerUi, e já o torna detectavel para aparecer la (visto que é um tipo interno nessa classe. Até entao nao aparecia como schema proprio la)
    @Getter
    @Builder
    public static class Objects {

        @ApiModelProperty(example = "preço")
        private String name;

        @ApiModelProperty(example = "O preço é obrigatório")
        private String userMessage;
    }
}
