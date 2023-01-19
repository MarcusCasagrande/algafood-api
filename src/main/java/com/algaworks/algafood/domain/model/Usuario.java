package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Usuario {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, columnDefinition = "datetime")
    @CreationTimestamp
    //@JsonIgnore
    private OffsetDateTime dataCadastro;

    @Column(nullable = false)
    @ManyToMany
    @JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private Set<Grupo> grupos = new HashSet<>();

    public boolean isNovo(){
        return (getId() == null);
    }

    // 23.15: removidos, pois agora se usará BCrypt
//    public boolean senhaCoincideCom(String senha) {
//        return getSenha().equals(senha);
//    }
//
//    public boolean senhaNaoCoincideCom(String senha) {
//        return !senhaCoincideCom(senha);
//    }

    public boolean adicionarGrupo(Grupo g){
        return getGrupos().add(g);
    }

    public boolean removerGrupo(Grupo g){
        return getGrupos().remove(g);
    }
}
