package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long> {

    // nome do metodo ja define que vai ser procurado e encontrado um user com o email especificado
    Optional<Usuario> findByEmail(String email);

}
