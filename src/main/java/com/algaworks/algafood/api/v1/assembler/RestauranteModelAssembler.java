package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired // classe de ModelMapperconfig de @Config instancia isso
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public RestauranteModelAssembler(){
        super(RestauranteController.class, RestauranteModel.class);
    }

    public RestauranteModel toModel(Restaurante restaurante) {
        /*
        RestauranteModel restauranteModel = new RestauranteModel();
        restauranteModel.setId(restaurante.getId());
        restauranteModel.setNome(restaurante.getNome());
        restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());

        CozinhaModel cozinhaModel = new CozinhaModel();
        cozinhaModel.setId(restaurante.getCozinha().getId());
        cozinhaModel.setNome(restaurante.getCozinha().getNome());
        restauranteModel.setCozinha(cozinhaModel);
        return restauranteModel;
         */
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);
        if (algaSecurity.autenticadoEEscopoLeitura()) {
            restauranteModel.add(algaLinks.linkToRestaurantes("restaurantes"));
            restauranteModel.add(algaLinks.linkToProdutos(restaurante.getId(), "produtos"));
            restauranteModel.getCozinha().add(algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
            if (restaurante.getEndereco() != null && restaurante.getEndereco().getCidade() != null) {
                restauranteModel.getEndereco().getCidade().add(algaLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
            }
            restauranteModel.add(algaLinks.linkToRestauranteFormasPagamento(restaurante.getId(), "formas-pagamento"));
        }
        //restauranteModel.getCozinha().add(algaLinks.linkToCozinhas("cozinhas"));
        //restauranteModel.getEndereco().getCidade().add(algaLinks.linkToCidades("cidades"));
        if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
            restauranteModel.add(algaLinks.linkToResponsaveisRestaurante(restaurante.getId(), "responsaveis"));
        }
        if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
            if (restaurante.getAtivo()) {
                restauranteModel.add(algaLinks.linkToInativarRestaurante(restaurante.getId(), "desativar"));
            } else {
                restauranteModel.add(algaLinks.linkToAtivarRestaurante(restaurante.getId(), "ativar"));
            }
        }
        if (algaSecurity.podeGerenciarFuncionamentoRestaurantes(restaurante.getId())) {
            if (restaurante.getAberto()) {
                restauranteModel.add(algaLinks.linkToFecharRestaurante(restaurante.getId(), "fechar"));
            } else {
                restauranteModel.add(algaLinks.linkToAbrirRestaurante(restaurante.getId(), "abrir"));
            }
        }
        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities){
        CollectionModel<RestauranteModel> collectionModel = super.toCollectionModel(entities);

        if (algaSecurity.autenticadoEEscopoLeitura()) {
            collectionModel.add(algaLinks.linkToRestaurantes());
        }

        return collectionModel;
    }
}
