package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * KEYWORD Docs: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
 */
@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {
    //nome vira varavel de instancia,  E pode escrever qualquer merda entre "find" e "By" sem efeito pratico
    List<Cozinha> findTodasByNome(String nome);

    // Qualquer select que queira trazer paginavel, é só mudar o return e incluir um pageable, que o spring dá conta do resto
    Page<Cozinha> findTodasByNome(String nome, Pageable pageable);

    //findBy ele detecta o que por depois.
    Optional<Cozinha> findByNome(String nome);

    //Containing é uma flag que serve como "like"
    List<Cozinha> findTodasByNomeContaining(String nome);

    // exists só retorna se tem ou nao
    boolean existsByNome(String nome);




}
