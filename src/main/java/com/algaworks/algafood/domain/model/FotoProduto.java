package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class FotoProduto {

    // nao tem @GeneratedValue pois a tabela nao tem chave incremental, apenas uma foreign de produto
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "produto_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY) // a maioria das vezes que trazer uma foto do produto, nao será necessário trazer o produto junto. Entao esse lazy evita que isso ocorra
    @MapsId //Produto é mapeado atraves do id da entidade FotoProduto (foreign key (id) acima)
    private Produto produto;

    @NotBlank
    @Column(nullable = false)
    private String nomeArquivo;
    private String descricao;
    private String contentType;
    private Long tamanho;

    public Long getRestauranteId(){
        if (getProduto() != null){
            return this.getProduto().getRestaurante().getId();
        }
        return null;
    }
}
