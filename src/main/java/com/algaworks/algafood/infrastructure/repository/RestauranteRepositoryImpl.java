package com.algaworks.algafood.infrastructure.repository;


import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.algaworks.algafood.domain.spec.RestauranteSpecs.comFretegratis;
import static com.algaworks.algafood.domain.spec.RestauranteSpecs.comNomeSemelhante;

/**
 * DISCLAIMER: Importante o nome terminar com "Impl" pro JPQL detectar ser uma implementacao customizada
 */
@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired @Lazy
    private RestauranteRepository restauranteRepository;

    public List<Restaurante> findWithJPQL(String nome, BigDecimal txInicial, BigDecimal txFinal){
        // "%" nao vale aqui como nos outros lugares. Dá pau
        var jpql = new StringBuilder();
        jpql.append("from Restaurante where 0 = 0 ");
        var parametros = new HashMap<String, Object>();

        if (StringUtils.hasLength(nome)){ // se nao é nulo nem vazio
            jpql.append("and nome like :nome ");
            parametros.put("nome", "%" + nome + "%");
        }
        if (txInicial != null){
            jpql.append("and taxaFrete >= :taxaInicial ");
            parametros.put("taxaInicial", txInicial);
        }
        if (txFinal != null){
            jpql.append("and taxaFrete <= :taxaFinal ");
            parametros.put("taxaFinal", txFinal);
        }
        TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);

        parametros.forEach((chave, valor) -> query.setParameter(chave, valor));
        return query.getResultList();
    }

    // aqui o nome do método nao tem relevancia pro JPQL. Pode ser o que quiser
    @Override
    public List<Restaurante> find(String nome, BigDecimal txInicial, BigDecimal txFinal){

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
        Root<Restaurante> root = criteria.from(Restaurante.class); // from Restaurante
        var predicates = new ArrayList<Predicate>();
        if (StringUtils.hasText(nome)){
            predicates.add(builder.like(root.get("nome"),"%" + nome + "%"));
        }
        if (txInicial != null){
            predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), txInicial));
        }
        if (txFinal != null){
            predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), txFinal));
        }
        criteria.where(predicates.toArray(new Predicate[0])); // 0 é redundante
        TypedQuery<Restaurante> query =  manager.createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public List<Restaurante> findComFreteGratis(String nome) {
        return restauranteRepository.findAll(comFretegratis().and(comNomeSemelhante(nome)));
    }


}
