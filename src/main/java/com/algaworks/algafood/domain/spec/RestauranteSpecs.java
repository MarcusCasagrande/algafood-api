package com.algaworks.algafood.domain.spec;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

// factory de specs
public class RestauranteSpecs {

    public static Specification<Restaurante> comFretegratis(){
        //return new RestauranteComFreteGratisSpec();
        return (root, query, builder) -> builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
    }

    public static Specification<Restaurante> comNomeSemelhante(String nome){
        return (root, query, builder) -> builder.like(root.get("nome"), "%" + nome + "%");
    }
}
