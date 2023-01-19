package com.algaworks.algafood.core.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.stream.Collectors;

public class PageableTranslator {

    public static Pageable translate(Pageable pageable, Map<String, String> fieldsMapping){
        //System.out.println(pageable.getSort()); // <-- inicialmente nomeCliente: ASC
        // define que o sort selecionado no front-end seja devidamente captado e ordenado para um pageable aqui (Neste caso, de nomeCliente para cliente.nome, mapeado no PedidoController.traduzirPageable)
        var orders = pageable.getSort().stream()
                .filter(order -> fieldsMapping.containsKey(order.getProperty())) // antes de fazer o mapeamento, esse filtro define que só vai mapear as propriedades existentes no PedidoController.traduzirPageable. Senão, ignora
                .map(order -> new Sort.Order(order.getDirection(), fieldsMapping.get(order.getProperty())))
                .collect(Collectors.toList());
        //System.out.println(orders); // <-- finalmente, cliente.nome: ASC

        // returna um pageable(numero da pagina, tamanho da pagina).comEssaOrdenacao(orders definido acima)
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(orders));
    }
}
