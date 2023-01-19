package com.algaworks.algafood.api.v1.model.objectmodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

// RestauranteDTO
@ApiModel(value = "Restaurantee", description = "Representa um restaurante")
@Setter
@Getter
// nao é exigido que os atributos tenham o mesmo nome dos atributos da classe (Restaurante) representada (mas fortemente aconselhado se for fazer uso do Model mapper)
public class RestauranteModel extends RepresentationModel<RestauranteModel> {

    @ApiModelProperty(example = "1")
    //@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class}) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    private Long id;

    @ApiModelProperty(example = "Thay")
    //@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class}) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    private String nome;

    @ApiModelProperty(example = "2.30")
    //@JsonView(RestauranteView.Resumo.class) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    private BigDecimal taxaFrete; // antes usado "precoFrete" pro modelMapper. Mas foi tirado.

   //@JsonView(RestauranteView.Resumo.class) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    private CozinhaModel cozinha;

    @ApiModelProperty(example = "true")
    private Boolean ativo;

    @ApiModelProperty(example = "true")
    private Boolean aberto;

    private EnderecoModel endereco;
}
