package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.repository.CustomJpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

// repositorio generico com metodo pra todos os reps
public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

    // privado eM pq vai ser usado aqui e a super (SimpleJpaRepository) nao tem essa variavel pra gente mexer
    private EntityManager manager;

    // construtor necessario pois a classe pai (SimpleJpaRepository) nao tem construtor padrao
    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager manager) {
        super(entityInformation, manager);
        this.manager = manager;
    }

    @Override
    public Optional<T> buscarPrimeiro() { // getDomainClass() vem de cima, vai ser o <T>
        var jpql = "from " + getDomainClass().getName();
        T entity = manager.createQuery(jpql, getDomainClass())
                .setMaxResults(1)  // <--- tudo basicamente pra fazer isso pra todos os repositorios
                .getSingleResult(); // pra retornar apenas um result. E a linha de cima ja limita buscar apenas um mesmo.
        return Optional.ofNullable(entity);
    }

    @Override
    public void detach(T entity) {
        manager.detach(entity);
    }
}
