package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroProdutoService {

    public static final String MSG_PRODUTO_EM_USO = "Produto de código %d não pode ser removido, pois está em uso";

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Produto salvar(Produto produto){
        return produtoRepository.save(produto);
    }

    public Produto buscarOuFalhar(Long restauranteId, Long produtoId){
        return produtoRepository.findById(restauranteId, produtoId).orElseThrow(
                () -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
    }
/*
    @Transactional
    public void excluir(Long id){
        try{
            produtoRepository.deleteById(id);
            produtoRepository.flush();
        } catch (EmptyResultDataAccessException e){
            throw new ProdutoNaoEncontradoException(id);
        } catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(MSG_PRODUTO_EM_USO, id));
        }
    }

    public Produto buscarProdutoDeRestaurante(Long restauranteId, Long produtoId){
        Restaurante r = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        Produto p = this.buscarOuFalhar(produtoId);
        if (!p.getRestaurante().getId().equals(restauranteId)){
            throw new ProdutoNaoEncontradoException(String.format("Não existe um cadastro de produto com código %d para o restaurante de código %d", produtoId, restauranteId));
        }
        return p;
    }

    @Transactional
    public void associarProduto(Long restauranteId, Long produtoId){
        Restaurante r = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        Produto p = buscarOuFalhar(produtoId);
        r.adicionarProduto(p);
    }

    @Transactional
    public void desassociarProduto(Long restauranteId, Long produtoId){
        Restaurante r = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        Produto p = buscarOuFalhar(produtoId);
        r.removerProduto(p);
    }
 */
}
