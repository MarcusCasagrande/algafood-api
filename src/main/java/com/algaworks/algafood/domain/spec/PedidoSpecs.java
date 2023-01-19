package com.algaworks.algafood.domain.spec;

import com.algaworks.algafood.api.v1.model.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

// factory de specs
public class PedidoSpecs {

    public static Specification<Pedido> usandoFiltro(PedidoFilter filtro){
        //return new RestauranteComFreteGratisSpec();
        return (root, query, builder) -> {
            // esse IF serve para quando o metodo é chamado com um Pageable, que força o spring a fazer um count.
            // Counts e fetch nao combinam, portanto o if pula os fetches em caso de estar fazendo um count.
            if (Pedido.class.equals(query.getResultType())) {
                // FETCH equivale ao "from Pedido p join fetch p.cliente..." mas na versao criteria. Usado pro problema do "N+1"
                root.fetch("restaurante").fetch("cozinha");
                root.fetch("cliente");
            }


            var predicates = new ArrayList<Predicate>();
            if (filtro.getClienteId() != null){
                predicates.add(builder.equal(root.get("cliente"), filtro.getClienteId()));
            }
            if (filtro.getRestauranteId() != null){
                predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
            }
            if (filtro.getDataCriacaoInicio() != null){
                predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
            }
            if (filtro.getDataCriacaoFim() != null){
                predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
            }

            return builder.and(predicates.toArray(new Predicate[0])); // [0] só pra transformar array em lista
        };
    }

    public static Specification<Restaurante> comNomeSemelhante(String nome){
        return (root, query, builder) -> builder.like(root.get("nome"), "%" + nome + "%");
    }
}
