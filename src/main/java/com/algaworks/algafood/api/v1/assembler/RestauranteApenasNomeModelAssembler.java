package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteIdNomeModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class RestauranteApenasNomeModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteIdNomeModel> {

    @Autowired // classe de ModelMapperconfig de @Config instancia isso
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public RestauranteApenasNomeModelAssembler(){
        super(RestauranteController.class, RestauranteIdNomeModel.class);
    }

    public RestauranteIdNomeModel toModel(Restaurante restaurante) {
        RestauranteIdNomeModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);
        if (algaSecurity.autenticadoEEscopoLeitura()) {
            restauranteModel.add(algaLinks.linkToRestaurantes("restaurantes"));
        }
//        restauranteModel.add(algaLinks.linkToAtivarRestaurante(restaurante.getId(), "ativar/desativar"));
//        if (restaurante.getAberto()){
//            restauranteModel.add(algaLinks.linkToFecharRestaurante(restaurante.getId(), "fechar"));
//        } else {
//            restauranteModel.add(algaLinks.linkToAbrirRestaurante(restaurante.getId(), "abrir"));
//        }

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteIdNomeModel> toCollectionModel(Iterable<? extends Restaurante> entities){
        var collectionModel = super.toCollectionModel(entities).add(linkTo(RestauranteController.class).withSelfRel());
        if (algaSecurity.autenticadoEEscopoLeitura()) {
            collectionModel.add(algaLinks.linkToRestaurantes());
        }
        return collectionModel;
    }
}
