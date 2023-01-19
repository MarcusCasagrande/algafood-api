package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.FotoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.FotoStorageService.NovaFoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;

@Service
public class CatalogoFotoProdutoService {

    @Autowired // é um agregado, nao tem repositorio proprio. Usa-se o de produto
    private ProdutoRepository produtoRepository;

    @Autowired
    private FotoStorageService fotoStorageService;

    @Transactional
    public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo){
        Long restauranteId = foto.getRestauranteId();
        Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, foto.getProduto().getId());
        String novoNomeArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
        String nomeArquivoExistente = null;
        if (fotoExistente.isPresent()){
            nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
            produtoRepository.delete(fotoExistente.get());
        }
        foto.setNomeArquivo(novoNomeArquivo);
        // primeiro salva dados de foto na base, depois insere no disco. Pois se der algum erro na insercao do disco, ele dá rollback
        foto = produtoRepository.save(foto);
        produtoRepository.flush(); // se for dar problema, que dê agora antes de inserir a foto
        NovaFoto novaFoto = NovaFoto.builder()
                .nomeArquivo(foto.getNomeArquivo())
                .contentType(foto.getContentType())
                .inputStream(dadosArquivo)
                .build();
        fotoStorageService.substituir(nomeArquivoExistente, novaFoto);
//      fotoStorageService.armazenar(novaFoto);
        return foto;
    }

    @Transactional
    public void deletar(Long restauranteId, Long produtoId){
        FotoProduto fotoProduto = buscarOuFalhar(restauranteId, produtoId);
        produtoRepository.delete(fotoProduto);
        produtoRepository.flush(); // se for dar problema, que dê agora antes de inserir a foto
        fotoStorageService.remover(fotoProduto.getNomeArquivo());
    }

    public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId){
        return produtoRepository.findFotoById(restauranteId, produtoId)
                .orElseThrow(() -> new FotoNaoEncontradaException(restauranteId, produtoId));
    }
}
