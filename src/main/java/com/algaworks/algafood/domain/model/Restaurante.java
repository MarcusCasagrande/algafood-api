package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.ValorZeroIncluiDescricao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante { // annotation constraints link: https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-builtin-constraints

    /**
     * retirandso todas as anotacoes JACKSON e pondo na classe mixin
     */
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull // <-- aceita vazio ""
   // @NotEmpty // <-- aceita espacos "   "
    //agora sim comporta todos:
    //@NotBlank(message = "Nome é obrigatório", groups = Groups.CadastroRestaurante.class)

    //@NotBlank // <-- comentado aqui pq vai ser usado na classe input
    @Column(nullable = false)
    private String nome;

    //@Column(name = "taxa_")
    //@NotNull // <-- comentado aqui pq vai ser usado na classe input
    //@PositiveOrZero(message = "{TaxaFrete.invalida}") // só funciona se for no ValidationMessages.prop, nao no messages.prop
    //@PositiveOrZero // igual a @DecimalMin("0")
    //@TaxaFrete
    //@Multiplo(numero = 5) // <-- comentado aqui pq vai ser usado na classe input
    @Column(nullable = false)
    private BigDecimal taxaFrete;

    //@JsonIgnoreProperties("hibernateLazyInitializer") // permite ignorar antes da criacao propriedades de Cozinha. Essa prop é de uma subclasse de cozinha criada por hibernare que só se ve o defeito com server error 500 em execucao ¬¬
    @JoinColumn(nullable = false)
    // quando for atualizar um restaurante, ignora a propriedade de cozinha definida (por exemplo, se o cara colocar "nome" no obj Json de cozinha)
    // allowGetters = true: ignora a prop (dá erro nessa aplicacao) se por "nome" no front end de cozinha, mas se for pra trazer o nome de cozinha para o front-end, dae agora permite
    //@JsonIgnoreProperties(value = "nome", allowGetters = true)
    //@JsonIgnore
    @ManyToOne//(fetch = FetchType.LAZY) //anotacoes "toOne" fazem eager loading
    //@JoinColumn(name = "cozinha_id") // pra quando quer renomear o foreign_key pra outra coisa

    // 3 comentados pq tao sendo usados em input
    //@NotNull//(groups = Groups.CadastroRestaurante.class)
    //@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)
    //@Valid // determina que Cozinha tambem tenha seus atributos @Constraints verificados (modo cascata)
    private Cozinha cozinha;

    //@JsonIgnore
    @Embedded
    private Endereco endereco;

    // anotacao hibernate que define que a propriedade é posta ja como data de cadastro
    @CreationTimestamp // anotacao do hibrnate, e nao do JPA. Se mudar pra outra implementacao, nao funciona mais. Mas como provavel que sempre será IMPLEMENTACAO hibernate, provavel que nao seja problema
    //@JsonIgnore
    @Column(nullable = false, columnDefinition = "datetime") // datetime tira os ms.. se por "datetime(6)", pega as 6 casas apos seg. (mili e micro s)
    private OffsetDateTime dataCadastro;

    @UpdateTimestamp // anot que define automaticamente que é atualizado quando der atualizacao de dado
    //@JsonIgnore
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataAtualizacao;

    private Boolean ativo = true;

    private Boolean aberto = Boolean.FALSE;

    //@JsonIgnore //se termina com @...toMany é LAZY por padrao
    @ManyToMany // spring.jpa.generate-ddl=true ja vai criar a tabela intermediaria assim que upar
    @JoinTable(name = "restaurante_forma_pagamento",  // customiza tal tabela
            joinColumns = @JoinColumn(name = "restaurante_id"), // diz que essa tabela vai ter o id de relacionamento com Restaurante via esse nome de id
            inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id")) //  mesmo que o de cima, mas ao inves de ir la em formaPagamento fazer isso, o @inverse ja permite fazer aqui
    private Set<FormaPagamento> formasPagamento = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "restaurante_usuario_responsavel",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> usuariosResponsaveis = new HashSet<>();


    @OneToMany(mappedBy = "restaurante")
    //@JsonIgnore
    private List<Produto> produtos = new ArrayList<>();

    public void ativar(){
        setAtivo(true);
    }

    public void inativar(){
        setAtivo(false);
    }

    public boolean adicionarFormaPagamento(FormaPagamento fp){
        return getFormasPagamento().add(fp);
    }

    public boolean removerFormaPagamento(FormaPagamento fp){
        return getFormasPagamento().remove(fp);
    }

    public boolean adicionarResponsavel(Usuario u){
        return getUsuariosResponsaveis().add(u);
    }

    public boolean removerResponsavel(Usuario u){
        return getUsuariosResponsaveis().remove(u);
    }

    public void fechar(){
        setAberto(false);
    }

    public void abrir(){
        setAberto(true);
    }

    public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
        return getFormasPagamento().contains(formaPagamento);
    }

    public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
        return !aceitaFormaPagamento(formaPagamento);
    }
}
