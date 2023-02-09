package com.algaworks.algafood.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder // nao tem construtores. Pra instanciar tem que fazer Problema.builder().<nome do campo>(<valor>)
@Schema(name = "Problema")
public class Problem {

    @Schema(example = "400")
    private Integer status;

    @Schema(example = "2023-01-20T12:00:50.1234567890")
    private OffsetDateTime timestamp;

    @Schema(example = "https://algafood.com.br/dados-invalidos")
    private String type;

    @Schema(example = "Dados inválidos")
    private String title;

    @Schema(example = "Um ou mais campos estão inválidos.")
    private String detail;

    @Schema(example = "Um ou mais campos estão inválidos.")
    private String userMessage; // <-- extensao, nao é padrao da especificacao. Mensagem para ajudar a informar o usuario final

    @Schema(description = "Lista de objetos ou campos que geraram o erro")
    private List<Objects> objects;


    @Getter
    @Builder
    @Schema(name = "ObjetoProblema")
    public static class Objects {

        @Schema(example = "preço")
        private String name;

        @Schema(example = "O preço é inválido")
        private String userMessage;
    }
}
