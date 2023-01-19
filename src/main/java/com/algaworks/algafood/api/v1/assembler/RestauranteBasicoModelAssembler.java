package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class RestauranteBasicoModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteBasicoModel> {

    @Autowired // classe de ModelMapperconfig de @Config instancia isso
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public RestauranteBasicoModelAssembler(){
        super(RestauranteController.class, RestauranteBasicoModel.class);
    }

    public RestauranteBasicoModel toModel(Restaurante restaurante) {
        RestauranteBasicoModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);
        if (algaSecurity.autenticadoEEscopoLeitura()) {
            restauranteModel.add(algaLinks.linkToRestaurantes("restaurantes"));
            restauranteModel.getCozinha().add(algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
        }
        //restauranteModel.getCozinha().add(algaLinks.linkToCozinhas("cozinhas"));


//        restauranteModel.add(algaLinks.linkToAtivarRestaurante(restaurante.getId(), "ativar/desativar"));
//        if (restaurante.getAberto()){
//            restauranteModel.add(algaLinks.linkToFecharRestaurante(restaurante.getId(), "fechar"));
//        } else {
//            restauranteModel.add(algaLinks.linkToAbrirRestaurante(restaurante.getId(), "abrir"));
//        }

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteBasicoModel> toCollectionModel(Iterable<? extends Restaurante> entities){
        CollectionModel<RestauranteBasicoModel> collectionModel = super.toCollectionModel(entities);

        if (algaSecurity.autenticadoEEscopoLeitura()) {
            collectionModel.add(algaLinks.linkToRestaurantes());
        }

        return collectionModel;

    }
}
