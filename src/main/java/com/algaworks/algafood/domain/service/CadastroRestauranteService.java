package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroRestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamentoService;

    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @Transactional // metodos publicos da clase de serviço, se recomenda ter transaction
    public Restaurante salvar(Restaurante restaurante){
        Long cozinhaId = restaurante.getCozinha().getId();
        Long cidadeId = restaurante.getEndereco().getCidade().getId();
        Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
        Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);
        restaurante.setCozinha(cozinha);
        restaurante.getEndereco().setCidade(cidade);
        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public void excluir(Long id){
        try {
            restauranteRepository.deleteById(id);
            restauranteRepository.flush();
        } catch (EmptyResultDataAccessException e){
            throw new RestauranteNaoEncontradoException(id);
        } catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format("Restaurante de código %d não pode ser removido, pois há um ou mais produtos vinculados.", id));
        }
    }

    @Transactional
    public void ativar(Long restauranteId){
        Restaurante r = buscarOuFalhar(restauranteId);
        r.setAtivo(true);
        // instrucao nao necessaria, pois o "r" está attached no banco via jpa. Tudo que for alterado no objeto, será sincronizado no database ao final da transacao
        //restauranteRepository.save(r);
    }

    @Transactional
    public void inativar(Long restauranteId){
        Restaurante r = buscarOuFalhar(restauranteId);
        r.inativar();
    }

    @Transactional // pois se der erro no segundo item, o primeiro dará rollback
    public void ativar(List<Long> restauranteIds){
        restauranteIds.forEach(this::ativar);
    }

    @Transactional // pois se der erro no segundo item, o primeiro dará rollback
    public void inativar(List<Long> restauranteIds){
        restauranteIds.forEach(this::inativar);
    }

    @Transactional
    public void fechar(Long restauranteId){
        Restaurante r = buscarOuFalhar(restauranteId);
        r.fechar();
    }

    @Transactional
    public void abrir(Long restauranteId){
        Restaurante r = buscarOuFalhar(restauranteId);
        r.abrir();
    }

    @Transactional
    public void associarformaPagamento(Long restauranteId, Long formaPagamentoId){
        Restaurante r = buscarOuFalhar(restauranteId);
        FormaPagamento fp = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
        r.adicionarFormaPagamento(fp);
    }

    @Transactional
    public void desassociarformaPagamento(Long restauranteId, Long formaPagamentoId){
        Restaurante r = buscarOuFalhar(restauranteId);
        FormaPagamento fp = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);
        r.removerFormaPagamento(fp);
    }

    @Transactional
    public void associarResponsavel(Long restauranteId, Long usuarioId){
        Restaurante r = buscarOuFalhar(restauranteId);
        Usuario u = cadastroUsuarioService.buscarOuFalhar(usuarioId);
        r.adicionarResponsavel(u);
    }

    @Transactional
    public void desassociarResponsavel(Long restauranteId, Long usuarioId){
        Restaurante r = buscarOuFalhar(restauranteId);
        Usuario u = cadastroUsuarioService.buscarOuFalhar(usuarioId);
        r.removerResponsavel(u);
    }

    public Restaurante buscarOuFalhar(Long id){
        return restauranteRepository.findById(id).orElseThrow(
                () -> new RestauranteNaoEncontradoException(id));
    }
}
