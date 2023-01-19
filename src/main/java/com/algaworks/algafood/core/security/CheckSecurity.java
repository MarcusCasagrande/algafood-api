package com.algaworks.algafood.core.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

// 23.23
public @interface CheckSecurity {

    public @interface Cozinhas{
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_COZINHAS')")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {}

        @PreAuthorize("@algaSecurity.autenticadoEEscopoLeitura()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {}
    }

    public @interface Restaurantes{
        @PreAuthorize("@algaSecurity.podeGerenciarCadastroRestaurantes()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeGerenciarCadastro {}

        @PreAuthorize("@algaSecurity.podeGerenciarFuncionamentoRestaurantes(#restauranteId)") // 23.28: @ chama um beam dessa classe e o metodo pode por o argumento com # e o nome esperado no @PathVariable do controller
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeGerenciarFuncionamento {} // responsavel do restaurante poderá abrir, fechar, cadastrar seu proprio Res

        @PreAuthorize("@algaSecurity.autenticadoEEscopoLeitura()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {}
    }

    public @interface Pedidos{

        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        @PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or "  //23.29: (@postAutorize). O returnObject é uma var implicita na expressao pra obter o objecto de retorno do metodo NO CONTROLLER! (metodo buscar, que retorna um PedidoModel)
                + "@algaSecurity.usuarioAutenticadoIgual(returnObject.cliente.id) or "
                + "@algaSecurity.gerenciaRestaurante(returnObject.restaurante.id)")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeBuscar {}

        @PreAuthorize("@algaSecurity.podePesquisarPedidos(#filtro.clienteId, #filtro.restauranteId)")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodePesquisar {}

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and isAuthenticated()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeCriar {}

        @PreAuthorize("@algaSecurity.podeGerenciarPedidos(#codigoPedido)")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeGerenciarPedidos {}
    }

    public @interface FormasPagamento{

        @PreAuthorize("@algaSecurity.autenticadoEEscopoLeitura()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {}

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_FORMAS_PAGAMENTO')")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {}

    }

    public @interface Cidades{

        @PreAuthorize("@algaSecurity.autenticadoEEscopoLeitura()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {}

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_CIDADES')")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {}

    }

    public @interface Estados{

        @PreAuthorize("@algaSecurity.autenticadoEEscopoLeitura()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {}

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_ESTADOS')")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {}

    }

    public @interface UsuariosGruposPermissoes {

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and @algaSecurity.usuarioAutenticadoIgual(#usuarioId)")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeAlterarPropriaSenha{}

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and "
                + "(hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES') or @algaSecurity.usuarioAutenticadoIgual(#usuarioId))")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeAlterarUsuario{}

        @PreAuthorize("@algaSecurity.podeEditarUsuariosGruposPermissoes()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar{}

        @PreAuthorize("@algaSecurity.podeVerificarUsuario()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar{}
    }

    public @interface Estatisticas {

        @PreAuthorize("@algaSecurity.podeConsultarEstatisticas()")
        @Retention(RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar{}
    }

}
