package com.algaworks.algafood.core.security;

import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component // 23.17 classe pra pegar os claims do jwt
public class AlgaSecurity {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication(); // objeto que representa o token que está autenticando a requisicao atual
    }

    public Long getUsuarioId(){
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();
        return Long.parseLong(jwt.getClaim("usuario_id"));
        //resumo de tudo em uma linha:
        // return ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("usuario_id");
    }

    public boolean gerenciaRestaurante(Long restauranteId){ // 23.28
        if (restauranteId == null) {
            return false;
        }
        return restauranteRepository.existsResponsavel(restauranteId, getUsuarioId());
    }

    public boolean podeAlterarStatus(String codigo){ //23.31: fluxo feito diferente no desafio. Acredito que o meu esteja correto
        Long restauranteId = pedidoRepository.pegarRestauranteIdPeloPedido(codigo);
        return gerenciaRestaurante(restauranteId);
    }

    // (23.38) pra evitar o problema de (@algaSecurity.getUsuarioId() == #filtro.clienteId) em CheckSecurity (onde tanto o usuario pode ta nulo por nao ter no jwt como no filtro o user pode nao passar nenhum user (nulo tbm)
    public boolean usuarioAutenticadoIgual(Long usuarioId){
        return getUsuarioId() != null && usuarioId != null && getUsuarioId().equals(usuarioId);
    }

    //23.39: metodos para gerenciar se mostra ou nao o link HAL dependendo da permissao
    public boolean hasAuthority(String authorityName){
        return getAuthentication().getAuthorities().stream().
                anyMatch(auth -> auth.getAuthority().equals(authorityName));
    }

    public boolean podeGerenciarPedidos(String codigoPedido){
        return hasAuthority("SCOPE_WRITE") && (hasAuthority("GERENCIAR_PEDIDOS") || podeAlterarStatus(codigoPedido));
    }

    public boolean podeVerificarUsuario(){
        //@PreAuthorize("hasAuthority('SCOPE_READ') and hasAuthority('CONSULTAR_USUARIOS_GRUPOS_PERMISSOES')")
        return hasAuthority("SCOPE_READ") && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
    }

    public boolean isAutenticado() {
        return getAuthentication().isAuthenticated();
    }

    public boolean temEscopoEscrita() {
        return hasAuthority("SCOPE_WRITE");
    }

    public boolean temEscopoLeitura() {
        return hasAuthority("SCOPE_READ");
    }

    public boolean autenticadoEEscopoLeitura() {
        return temEscopoLeitura() && isAutenticado();
    }

    public boolean podeGerenciarCadastroRestaurantes() {
        return temEscopoEscrita() && hasAuthority("EDITAR_RESTAURANTES");
    }

    public boolean podeGerenciarFuncionamentoRestaurantes(Long restauranteId) {
        return temEscopoEscrita() && (hasAuthority("EDITAR_RESTAURANTES")
                || gerenciaRestaurante(restauranteId));
    }

    public boolean podeConsultarUsuariosGruposPermissoes() {
        return temEscopoLeitura() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
    }

    public boolean podeEditarUsuariosGruposPermissoes() {
        return temEscopoEscrita() && hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES");
    }

    public boolean podePesquisarPedidos(Long clienteId, Long restauranteId) {
        return temEscopoLeitura() && (hasAuthority("CONSULTAR_PEDIDOS")
                || usuarioAutenticadoIgual(clienteId) || gerenciaRestaurante(restauranteId));
    }

    public boolean podeConsultarEstatisticas() {
        return temEscopoLeitura() && hasAuthority("GERAR_RELATORIOS");
    }

}
