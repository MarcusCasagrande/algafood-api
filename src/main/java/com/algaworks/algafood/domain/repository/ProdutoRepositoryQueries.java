package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.FotoProduto;

// interface pra executar a infraestrutura de salvar um FotoProduto
public interface ProdutoRepositoryQueries {

    FotoProduto save(FotoProduto fotoProduto);

    void delete(FotoProduto foto);
}
