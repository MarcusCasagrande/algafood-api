package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@JsonRootName("gastronomia") // tag <xml> vai msotrar isso ao inves de <Cozinha>
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
//@Table(name = "tab_cozinhas")
public class Cozinha {

    //@NotNull(groups = Groups.CozinhaId.class) // <-- comentado aqui pq vai ser usado na classe input
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@JsonProperty(value = "titulo") // postman cospe "titulo" ao inves de "nome"
    //@JsonIgnore
    //@NotBlank // <-- comentado aqui pq vai ser usado na classe input
    @Column(nullable = false)
    private String nome;

    // nao necessario neste projeto ate entao
    @OneToMany(mappedBy = "cozinha")
    //@JsonIgnore // lista de cozinha tem lista de restaurante que tem lista de cozinha.. JsonIgnore evita que isso seja serializado
    private List<Restaurante> restaurantes = new ArrayList<>(); // nao é obrigado a instanciar ja. mas é bom
}
