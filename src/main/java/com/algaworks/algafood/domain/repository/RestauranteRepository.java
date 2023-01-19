package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//find pode ser trocado por: read, query, get, stream.
// JpaSpecificationExecutor permite que RestauranteRepositoryImpl implemente um metodo findAll que retorna tipos Specification
@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante> {

//    @Query("from Restaurante r join r.cozinha left join fetch r.formasPagamento") // aula 6.14 uma especie de override do findAll() normal. Serve pra corrigir o N+1
    @Query("from Restaurante r join r.cozinha") // inseri @JsonIgnore em Restaurante.formasPagamento, entao o fetch nao precisa pq nao vai mais buscar
    List<Restaurante> findAll();

    //between
    List<Restaurante> findByTaxaFreteBetween(BigDecimal txInicial, BigDecimal txFinal);

    // and
    List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long id);

    // palavras-chave entre "find" e "by" (first: apenas o primeiro result. Inclusive pode insrir ainda qualquer merda depois do first e antes dfo by)
    Optional<Restaurante> findFirstByNomeContaining(String nome);

    // top X numero, os primeiros Xs
    List<Restaurante> findTop2ByNomeContaining(String nome);

    //count. perceba que o campo pega de dentro de cozinha que ta dentro de rest
    int countByCozinhaId(Long id);

    //JPQL (comentado o query se for usar o arquivo no META-INF
    //@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
    List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozId);

    //interface aqui, o spring detecta que tem uma classe customizada que implementa isso (RestauranteRepositoryImpl)
    //NOTA: metodo apagado para ser posto em outra interface (queries)
    //public List<Restaurante> find(String nome, BigDecimal txInicial, BigDecimal txFinal);

    // 23.28: implementado via orm.xml, pra lembrar como faz
    boolean existsResponsavel(Long restauranteId, Long usuarioId);

}
