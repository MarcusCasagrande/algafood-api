package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.objectmodel.EstadoModel;
import com.algaworks.algafood.api.v1.controller.EstadoController;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class EstadoModelAssembler extends RepresentationModelAssemblerSupport<Estado, EstadoModel>{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public EstadoModelAssembler() {
        super(EstadoController.class, EstadoModel.class);
    }

    public EstadoModel toModel(Estado estado){
        EstadoModel estadoModel = createModelWithId(estado.getId(), estado); // essas 2 linhas equivalem as 2 debaixo comentadas)
        modelMapper.map(estado, estadoModel);
//        CidadeModel cidadeModel =  modelMapper.map(cidade, CidadeModel.class);
//        cidadeModel.add(linkTo(methodOn(CidadeController.class).buscar(cidadeModel.getId())).withSelfRel());

        //estadoModel.add(linkTo(methodOn(EstadoController.class).listar()).withRel("estados"));
        if (algaSecurity.autenticadoEEscopoLeitura()){
            estadoModel.add(algaLinks.linkToEstados("estados"));
        }
        return estadoModel;
    }

    @Override
    public CollectionModel<EstadoModel> toCollectionModel(Iterable<? extends Estado> entities){
        var collectionModel = super.toCollectionModel(entities);
        if (algaSecurity.autenticadoEEscopoLeitura()){
            collectionModel.add(algaLinks.linkToEstados());
        }
        return collectionModel;
    }
}
