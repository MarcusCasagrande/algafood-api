package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastroUsuarioService {


    public static final String MSG_USUARIO_EM_USO = "Usuário de código %d não pode ser removido, pois está em uso";
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CadastroGrupoService cadastroGrupoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario){
        usuarioRepository.detach(usuario); // detachado pois senao ele fazia update num user a ser editado aqui
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)){
            throw new NegocioException(String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
        }
        if (usuario.isNovo())
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long id){
        try {
            usuarioRepository.deleteById(id);
            usuarioRepository.flush();
        } catch (EmptyResultDataAccessException e){
            throw new UsuarioNaoEncontradoException(id);
        } catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(MSG_USUARIO_EM_USO, id));
        }
    }

    public Usuario buscarOuFalhar(Long id){
        return usuarioRepository.findById(id).orElseThrow(
                () -> new UsuarioNaoEncontradoException(id));
    }

    @Transactional
    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }
        usuario.setSenha(passwordEncoder.encode(novaSenha));
    }

    @Transactional
    public void adicionarGrupo(Long usuarioId, Long grupoId){
        Usuario u = buscarOuFalhar(usuarioId);
        Grupo g = cadastroGrupoService.buscarOuFalhar(grupoId);
        u.adicionarGrupo(g);
    }

    @Transactional
    public void removerGrupo(Long usuarioId, Long grupoId){
        Usuario u = buscarOuFalhar(usuarioId);
        Grupo g = cadastroGrupoService.buscarOuFalhar(grupoId);
        u.removerGrupo(g);
    }
}
