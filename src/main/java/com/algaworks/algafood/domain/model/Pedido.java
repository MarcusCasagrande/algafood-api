package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.exception.NegocioException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false) // pra nao chamar o equals e hash da classe pai
@Entity
public class Pedido extends AbstractAggregateRoot<Pedido> {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String codigo;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal taxaFrete;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @CreationTimestamp
    //@JsonIgnore
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCriacao;

    @Column
    private OffsetDateTime dataConfirmacao;

    @Column
    private OffsetDateTime dataCancelamento;

    @Column
    private OffsetDateTime dataEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FormaPagamento formaPagamento;

    @ManyToOne //lembrando que manyToOne é "eager": ja faz consulta de restaurantes mesmo sem ser pedido
    @JoinColumn(nullable = false)
    private Restaurante restaurante;

    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;

    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING) // anotacao para que o jpa saiba que é um ENUM aqui quando for fazer o mapeamento, senão dá pau
    private StatusPedido status = StatusPedido.CRIADO;

    @Embedded
    private Endereco enderecoEntrega;

    // OnetoMany é lazy (só pega pedidos se for necessario. show)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL) // significa: ao salvar o pedido (metodo save do rep), salve tambem os itens dele. Se nao fizer isso, nao salva os itensPedido
    private List<ItemPedido> itens = new ArrayList<>();

    public void calcularValorTotal() {
        getItens().forEach(ItemPedido::calcularPrecoTotal);
        this.subtotal = getItens().stream()
                .map(item -> item.getPrecoTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotal = this.subtotal.add(this.taxaFrete);
    }

    public void definirFrete() {
        setTaxaFrete(getRestaurante().getTaxaFrete());
    }

    public void atribuirPedidoAosItens() {
        getItens().forEach(item -> item.setPedido(this));
    }

    public void confirmar(){
        setStatus(StatusPedido.CONFIRMADO);
        setDataConfirmacao(OffsetDateTime.now());

        registerEvent(new PedidoConfirmadoEvent(this));
    }

    public void entregar(){
        setStatus(StatusPedido.ENTREGUE);
        setDataEntrega(OffsetDateTime.now());
    }

    public void cancelar(){
        setStatus(StatusPedido.CANCELADO);
        setDataCancelamento(OffsetDateTime.now());
        registerEvent(new PedidoCanceladoEvent(this));
    }

    public boolean podeSerConfirmado(){
        return getStatus().podeAlterarPara(StatusPedido.CONFIRMADO);
    }
    public boolean podeSerEntregue(){
        return getStatus().podeAlterarPara(StatusPedido.ENTREGUE);
    }
    public boolean podeSerCancelado(){
        return getStatus().podeAlterarPara(StatusPedido.CANCELADO);
    }

    private void setStatus(StatusPedido novoStatus){ // privado pra ninguem mais de fora poder alterar status
        if (getStatus().naoPodeAlterarPara(novoStatus)){
            throw new NegocioException(
                    String.format("Status do pedido %s não pode ser alterado de %s para %s",
                            getCodigo(), getStatus().getDescricao(), novoStatus.getDescricao()));
        }
        this.status = novoStatus;
    }

    @PrePersist // metodo de callback do JPA. Diz que antes de persistir, executa esse metodo
    private void gerarCodigo(){
        setCodigo(UUID.randomUUID().toString());
    }

}
