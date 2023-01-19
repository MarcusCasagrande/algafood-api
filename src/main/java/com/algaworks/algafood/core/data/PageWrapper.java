package com.algaworks.algafood.core.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PageWrapper<T> extends PageImpl<T> { // classe que faz o wrapper do Pageable pra corrigir um "bug" quando troca o nome da propriedade no link do get (PedidoController.traduzirPageable, aula 19.17. Nao entendi nada)

    private Pageable pageable;

    public PageWrapper(Page<T> page, Pageable pageable) {
        super(page.getContent(), pageable, page.getTotalElements());

        this.pageable = pageable;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

}
