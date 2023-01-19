package com.algaworks.algafood.core.security.authorizationserver;

import com.algaworks.algafood.domain.model.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

// classe criada pra adicionar algumas propriedades necessarias que nao estao por padrao na classe User do spring
@Getter
public class AuthUser extends User {

    private Long userId;
    private String fullName;

    public AuthUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities){ // username é o email, aparentemente
        super(usuario.getEmail(), usuario.getSenha(), authorities);
        this.userId = usuario.getId();
        this.fullName = usuario.getNome();
    }
}
