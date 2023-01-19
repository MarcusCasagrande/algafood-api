package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCozinhaService {

    public static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe um cadastro de cozinha com código %d";
    public static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";
    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Transactional // pode fazer 2 metodos difrentes pra criar e atualizar. Neste caso a aula usa apenas um metodo pra ambos
    public Cozinha salvar(Cozinha cozinha){
        return cozinhaRepository.save(cozinha);
    }

    @Transactional
    public void excluir(Long id){
        try {
            cozinhaRepository.deleteById(id);
            cozinhaRepository.flush(); // precisa pra forcar a instrucao acima (delete) e ja trata excecoes aqui (nos catchs). Senao, o metodo acaba e só depois ele executa e nao capta a exception
        } catch (EmptyResultDataAccessException e){
            // exception base do spring (nao recomendado colocar HttpStatus aqui, mas funciona)
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Não existe um cadastro de cozinha com código %d", id));
            throw new CozinhaNaoEncontradaException(id);
        } catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(MSG_COZINHA_EM_USO, id));
        }
    }

    public Cozinha buscarOuFalhar(Long id){
        return cozinhaRepository.findById(id).orElseThrow(
                () -> new CozinhaNaoEncontradaException(id));
    }

}
