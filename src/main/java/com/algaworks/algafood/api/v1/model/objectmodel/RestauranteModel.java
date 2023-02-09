package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

// RestauranteDTO
@Setter
@Getter
// nao é exigido que os atributos tenham o mesmo nome dos atributos da classe (Restaurante) representada (mas fortemente aconselhado se for fazer uso do Model mapper)
public class RestauranteModel extends RepresentationModel<RestauranteModel> {

    //@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class}) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    @Schema(example = "1")
    private Long id;

    //@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class}) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    @Schema(example = "Tuc-tuc")
    private String nome;

    //@JsonView(RestauranteView.Resumo.class) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    @Schema(example = "1.50")
    private BigDecimal taxaFrete; // antes usado "precoFrete" pro modelMapper. Mas foi tirado.

   //@JsonView(RestauranteView.Resumo.class) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    private CozinhaModel cozinha;

    @Schema(example = "true")
    private Boolean ativo;

    @Schema(example = "true")
    private Boolean aberto;

    private EnderecoModel endereco;
}
