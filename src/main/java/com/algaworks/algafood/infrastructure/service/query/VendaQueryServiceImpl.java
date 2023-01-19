package com.algaworks.algafood.infrastructure.service.query;

import com.algaworks.algafood.api.v1.model.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Metodo(s) com provavelmnte muito codigo de infraesterutura, por isso implementado aqui e nao no service
// Esta classe nao precisaria ter o padrao Impl. Apenas usando por convencao. Implemente diretamente por nós a interface
// Aqui nao vem coisas de repositorio como "buscarPedido". Aqui é só pra consultas que nada tme a ver com agregados
@Repository // diferentemente de um @Service, o spring traduz algumas excecoes quando se trata de uma annotation @Repository
public class VendaQueryServiceImpl implements VendaQueryService {

    @PersistenceContext
    private EntityManager manager;

    /**
     * SELECT A SER FEITO:
     * select date(p.data_criacao) as data_criacao,
     *  count(p.id) as total_vendas,
     *  sum(p.valor_total) as total_faturado
     * from pedido p
     * group by date(p.data_criacao)
     */
    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
        var builder = manager.getCriteriaBuilder();
        var query = builder.createQuery(VendaDiaria.class); // o tipo de retorno
        var root = query.from(Pedido.class); // consulta vem de Pedido

        // select convert_tz('2019-11-03 02:00:30', '+00:00', '-03:00') (funcao pra horario de brasilia)
        var functionConvertTzDataCriacao = builder.function("convert_tz", Date.class,
                root.get("dataCriacao"), builder.literal("+00:00"), builder.literal(timeOffset));
        // date(p.data_criacao): params: funcao do banco (date), tipo de retorno do java, argumentos q vao na funcao (nesse caso apenas 1)
        // select date(convert_tz('2019-11-03 02:00:30', '+00:00', '-03:00')
        var functionDateDataCriacao = builder.function("date", Date.class, functionConvertTzDataCriacao);

        // selection params: a classe destino, e todos os campos a serem trazidos: date, Long e double
        var selection = builder.construct(VendaDiaria.class,
                functionDateDataCriacao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal"))
        );
        //importante: construtor @AllArgsConstructor de VendaDiaria deve ter atributos a ordem igual do selection acima

        query.select(selection);
        query.groupBy(functionDateDataCriacao);

        var predicates = new ArrayList<Predicate>();
        predicates.add(root.get("status").in(StatusPedido.ENTREGUE, StatusPedido.CONFIRMADO));
        if (filtro.getRestauranteId() != null){
            predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
        }
        if (filtro.getDataCriacaoInicio() != null){
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
        }
        if (filtro.getDataCriacaoFim() != null){
            predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
        }
        query.where(predicates.toArray(new Predicate[0]));

        return manager.createQuery(query).getResultList();
    }

    //para um puro SQL, usar:
    // Query nativeQuery = manager.createNativeQuery(String sql)
    // query.setParameter(x, x)
    // (List<VendaDiaria>) nativeQuery.getResultList();
}
