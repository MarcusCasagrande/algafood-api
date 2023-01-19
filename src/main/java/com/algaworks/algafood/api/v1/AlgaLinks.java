package com.algaworks.algafood.api.v1;

import com.algaworks.algafood.api.v1.controller.*;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.TemplateVariable.VariableType;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component //classe criada pra injetar links em PedidoModelAssembler (e talvez outros assemblers mais depois) pra cortar codigo/responsabilidade de la (19.20)
public class AlgaLinks {

    private static final String SELF = IanaLinkRelations.SELF.value();

    // virou constante pois pode ser reaproveitado {?page,size,sort} (19.20)
    public static final TemplateVariables PAGINACAO_VARIABLES = new TemplateVariables(    // TEMPLATES: 19.18
            new TemplateVariable("page", VariableType.REQUEST_PARAM),
            new TemplateVariable("size", VariableType.REQUEST_PARAM),
            new TemplateVariable("sort", VariableType.REQUEST_PARAM)
    );

    public static final TemplateVariables PROJECAO_VARIABLES = new TemplateVariables(
            new TemplateVariable("projecao", VariableType.REQUEST_PARAM));

    public Link linkToPedidos(String rel){

        TemplateVariables filtroVariables = new TemplateVariables(    // TEMPLATES: 19.18
                new TemplateVariable("clientId", VariableType.REQUEST_PARAM),
                new TemplateVariable("restauranteId", VariableType.REQUEST_PARAM),
                new TemplateVariable("dataCriacaoInicio", VariableType.REQUEST_PARAM),
                new TemplateVariable("dataCriacaoFim", VariableType.REQUEST_PARAM)
        );
        String pedidosUrl = linkTo(PedidoController.class).toUri().toString();
        return Link.of(UriTemplate.of(pedidosUrl, PAGINACAO_VARIABLES.concat(filtroVariables)), rel);
    }

    //pedidoModel.getRestaurante().add(linkTo(methodOn(RestauranteController.class).buscar(pedido.getRestaurante().getId())).withSelfRel());
    public Link linkToRestaurante(Long restauranteId){
        return linkToRestaurante(restauranteId, SELF);
    }
    public Link linkToRestaurante(Long restauranteId, String rel){
        return linkTo(methodOn(RestauranteController.class).buscar(restauranteId)).withRel(rel);
    }
    public Link linkToRestaurantes(){
        return linkToRestaurantes(SELF);
    }
    public Link linkToRestaurantes(String rel){
        String restauranteUrl = linkTo(RestauranteController.class).toUri().toString();
        Link link = Link.of(UriTemplate.of(restauranteUrl, PROJECAO_VARIABLES), rel);
        return link;
    }

    public Link linkToRestauranteFormasPagamento(Long restauranteId) {
        return linkToRestauranteFormasPagamento(restauranteId, SELF);
    }
    public Link linkToRestauranteFormasPagamento(Long restauranteId, String rel) {
        return linkTo(methodOn(RestauranteFormaPagamentoController.class)
                .listar(restauranteId)).withRel(rel);
    }
    public Link linkToRestauranteFormaPagamentoDesassociacao(Long restauranteId, Long formaPagamentoId, String rel){
        return linkTo(methodOn(RestauranteFormaPagamentoController.class).desassociar(restauranteId, formaPagamentoId)).withRel(rel);
    }
    public Link linkToRestauranteFormaPagamentoAssociacao(Long restauranteId, String rel){
        return linkTo(methodOn(RestauranteFormaPagamentoController.class).associar(restauranteId, null)).withRel(rel);
    }// quando o parametro posto como null veio como um @pathVariable no metodo original, o HATEOAS ja entende que é templated:true (19.29)

    //pedidoModel.getCliente().add(linkTo(methodOn(UsuarioController.class).buscar(pedido.getCliente().getId())).withSelfRel());
    public Link linkToUsuario(Long userId){
        return linkToUsuario(userId, SELF);
    }
    public Link linkToUsuario(Long userId, String rel){
        return linkTo(methodOn(UsuarioController.class).buscar(userId)).withRel(rel);
    }

    //pedidoModel.getFormaPagamento().add(linkTo(methodOn(FormaPagamentoController.class).buscar(pedido.getFormaPagamento().getId(), null)).withSelfRel());
    public Link linkToFormaPagamento(Long fpId){
        return linkToFormaPagamento(fpId, SELF);
    }
    public Link linkToFormaPagamento(Long fpId, String rel){
        return linkTo(methodOn(FormaPagamentoController.class).buscar(fpId, null)).withRel(rel);
    }
    public Link linkToFormasPagamento(){
        return linkToFormasPagamento(SELF);
    }
    public Link linkToFormasPagamento(String rel){
        return linkTo(FormaPagamentoController.class).withRel(rel);
    }

    //pedidoModel.getEnderecoEntrega().getCidade().add(linkTo(methodOn(CidadeController.class).buscar(pedido.getEnderecoEntrega().getCidade().getId())).withSelfRel());
    public Link linkToCidade(Long cidadeId){
        return linkToCidade(cidadeId, SELF);
    }
    public Link linkToCidade(Long cidadeId, String rel){
        return linkTo(methodOn(CidadeController.class).buscar(cidadeId)).withRel(rel);
    }

    //pedidoModel.getItens().stream().forEach(itemPedido -> itemPedido.add(linkTo(methodOn(ProdutoController.class).buscar(pedidoModel.getRestaurante().getId(), itemPedido.getProdutoId())).withRel("produto")));
    public Link linkToProduto(long restauranteId, long produtoId){
        return linkToProduto(restauranteId, produtoId, SELF);
    }
    public Link linkToProduto(long restauranteId, long produtoId, String rel){
        return linkTo(methodOn(RestauranteProdutoController.class).buscar(restauranteId, produtoId)).withRel(rel);
    }
    public Link linkToProdutos(Long restauranteId){
        return linkToProdutos(restauranteId, SELF);
    }
    public Link linkToProdutos(Long restauranteId, String rel){ // (19.31): passar null aqui ja cria templated:true com o link pro nome da variavel Boolean {incluirInativos} sem precisar ficar incluindo templates no método aparentemente
        return linkTo(methodOn(RestauranteProdutoController.class).listar(restauranteId, null)).withRel(rel);
    }

    public Link linkToFoto(Long restauranteId, Long produtoId){
        return linkToFoto(restauranteId, produtoId, SELF);
    }
    public Link linkToFoto(Long restauranteId, Long produtoId, String rel){
        return linkTo(methodOn(RestauranteProdutoFotoController.class).buscar(restauranteId, produtoId)).withRel(rel);
    }


    //cidadeModel.add(linkTo(methodOn(CidadeController.class).listar()).withRel("cidades")); // modo 3
    public Link linkToCidades(){
        return linkToCidades(SELF);
    }
    public Link linkToCidades(String rel){
        return linkTo(methodOn(CidadeController.class).listar()).withRel(rel);
    }

    //cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class).buscar(cidadeModel.getEstado().getId())).withSelfRel());
    public Link linkToEstado(long estadoId){
        return linkToEstado(estadoId, SELF);
    }
    public Link linkToEstado(long estadoId, String rel){
        return linkTo(methodOn(EstadoController.class).buscar(estadoId)).withRel(rel);
    }

    //estadoModel.add(linkTo(methodOn(EstadoController.class).listar()).withRel("estados"));
    public Link linkToEstados(){
        return linkToEstados(SELF);
    }
    public Link linkToEstados(String rel){
        return linkTo(methodOn(EstadoController.class).listar()).withRel(rel);
    }

    //usuarioModel.add(linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios")); // modo 3
    public Link linkToUsuarios(){
        return linkToUsuarios(SELF);
    }
    public Link linkToUsuarios(String rel){
        return linkTo(methodOn(UsuarioController.class).listar()).withRel(rel);
    }

    //usuarioModel.add(linkTo(methodOn(UsuarioGrupoController.class).listar(usuario.getId())).withRel("grupos-usuarioss"));
    public Link linkToGruposUsuarios(long userId){
        return linkToGruposUsuarios(userId, SELF);
    }
    public Link linkToGruposUsuarios(long userId, String rel){
        return linkTo(methodOn(UsuarioGrupoController.class).listar(userId)).withRel(rel);
    }

    public Link linkToUsuarioGrupoAssociar(Long usuarioid, String rel){
        return linkTo(methodOn(UsuarioGrupoController.class).associar(usuarioid, null)).withRel(rel); //vai templetar AUTO aqui
    }
    public Link linkToUsuarioGrupoDesassociar(Long usuarioid, Long grupoId, String rel){
        return linkTo(methodOn(UsuarioGrupoController.class).desassociar(usuarioid, grupoId)).withRel(rel);
    }

    //.add(linkTo(methodOn(this.getClass()).listar(restauranteId)).withSelfRel());
    public Link linkToResponsaveisRestaurante(long restauranteId){
        return linkToResponsaveisRestaurante(restauranteId, SELF);
    }
    public Link linkToResponsaveisRestaurante(long restauranteId, String rel){
        return linkTo(methodOn(RestauranteUsuarioController.class).listar(restauranteId)).withRel(rel);
    }
    public Link linkToResponsavelRestauranteDesassociacao(long restauranteId, Long usuarioId, String rel){
        return linkTo(methodOn(RestauranteUsuarioController.class).removerResponsavel(restauranteId, usuarioId)).withRel(rel);
    }
    public Link linkToResponsavelRestauranteAssociacao(long restauranteId, String rel){
        return linkTo(methodOn(RestauranteUsuarioController.class).adicionarResponsavel(restauranteId, null)).withRel(rel);
    } // templated: true

    //cozinhaModel.add(linkTo(CozinhaController.class).withRel("cozinhas"));
    public Link linkToCozinhas(){
        return linkToCozinhas(SELF);
    }
    public Link linkToCozinhas(String rel){
        return linkTo(CozinhaController.class).withRel(rel);
    }
    public Link linkToCozinha(Long cozinhaId){
        return linkToCozinha(cozinhaId, SELF);
    }
    public Link linkToCozinha(Long cozinhaId, String rel){
        return linkTo(methodOn(CozinhaController.class).buscar(cozinhaId)).withRel(rel);
    }

    public Link linkToConfirmacaoPedido(String codigoPedido, String rel){
        return linkTo(methodOn(FluxoPedidoController.class).confirmar(codigoPedido)).withRel(rel);
    }
    public Link linkToEntregaPedido(String codigoPedido, String rel){
        return linkTo(methodOn(FluxoPedidoController.class).entregar(codigoPedido)).withRel(rel);
    }
    public Link linkToCancelamentoPedido(String codigoPedido, String rel){
        return linkTo(methodOn(FluxoPedidoController.class).cancelar(codigoPedido)).withRel(rel);
    }

    public Link linkToAtivarRestaurante(Long restauranteId, String rel){
        return linkTo(methodOn(RestauranteController.class).ativar(restauranteId)).withRel(rel);
    }
    public Link linkToInativarRestaurante(Long restauranteId, String rel){
        return linkTo(methodOn(RestauranteController.class).inativar(restauranteId)).withRel(rel);
    }
    public Link linkToAbrirRestaurante(Long restauranteId, String rel){
        return linkTo(methodOn(RestauranteController.class).abertura(restauranteId)).withRel(rel);
    }
    public Link linkToFecharRestaurante(Long restauranteId, String rel){
        return linkTo(methodOn(RestauranteController.class).fechamento(restauranteId)).withRel(rel);
    }

    public Link linkToGrupos(){
        return linkToGrupos(SELF);
    }
    public Link linkToGrupos(String rel){
        return linkTo(GrupoController.class).withRel(rel);
    }

    public Link linkToGrupoPermissoes(Long grupoId){
        return linkToGrupoPermissoes(grupoId, SELF);
    }
    public Link linkToGrupoPermissoes(Long grupoId, String rel){
        return linkTo(methodOn(GrupoPermissaoController.class).listar(grupoId)).withRel(rel);
    }


    public Link linkToPermissoes(){
        return linkToPermissoes(SELF);
    }
    public Link linkToPermissoes(String rel){
        return linkTo(PermissaoController.class).withRel(rel);
    }

    public Link linkToDesassociarPermissao(Long grupoId, Long permissaoId){
        return linkToDesassociarPermissao(grupoId, permissaoId, SELF);
    }
    public Link linkToDesassociarPermissao(Long grupoId, Long permissaoId, String rel){
        return linkTo(methodOn(GrupoPermissaoController.class).desassociar(grupoId, permissaoId)).withRel(rel);
    }
    public Link linkToAssociarPermissao(Long grupoId){
        return linkToAssociarPermissao(grupoId, SELF);
    }
    public Link linkToAssociarPermissao(Long grupoId, String rel){
        return linkTo(methodOn(GrupoPermissaoController.class).associar(grupoId, null)).withRel(rel); // templete faz auto aqui
    }

    public Link linkToEstatisticas(){
        return linkToEstatisticas(SELF);
    }
    public Link linkToEstatisticas(String rel){
        return linkTo(EstatisticasController.class).withRel(rel);
    }
    public Link linkToEstatisticasVendasDiarias(String rel) {
        TemplateVariables filtroVariables = new TemplateVariables(
                new TemplateVariable("restauranteId", VariableType.REQUEST_PARAM),
                new TemplateVariable("dataCriacaoInicio", VariableType.REQUEST_PARAM),
                new TemplateVariable("dataCriacaoFim", VariableType.REQUEST_PARAM),
                new TemplateVariable("timeOffset", VariableType.REQUEST_PARAM));

        String pedidosUrl = linkTo(methodOn(EstatisticasController.class)
                .consultarVendasDiarias(null, null)).toUri().toString();

        return Link.of(UriTemplate.of(pedidosUrl, filtroVariables), rel);
    }
}
