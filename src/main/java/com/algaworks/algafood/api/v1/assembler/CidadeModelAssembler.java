package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.objectmodel.CidadeModel;
import com.algaworks.algafood.api.v1.controller.CidadeController;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component // num generico seria <T, S>
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public CidadeModelAssembler(){
        super(CidadeController.class, CidadeModel.class);
    }

    @Override
    public CidadeModel toModel(Cidade cidade) {
        CidadeModel cidadeModel = createModelWithId(cidade.getId(), cidade); // essas 2 linhas equivalem as 3 debaixo comentadas)
        modelMapper.map(cidade, cidadeModel);
//        CidadeModel cidadeModel =  modelMapper.map(cidade, CidadeModel.class);
//        Link link = linkTo(methodOn(CidadeController.class).buscar(cidadeModel.getId())).withSelfRel(); // 19.9
//        cidadeModel.add(link);

//      cidadeModel.add(Link.of("http://localhost:8080/cidades/", IanaLinkRelations.COLLECTION));
//      cidadeModel.add(Link.of("http://localhost:8080/cidades/", "cidades")); //modo 1
//      cidadeModel.add(linkTo(CidadeController.class).withRel("cidades")); // modo 2
        //cidadeModel.add(linkTo(methodOn(CidadeController.class).listar()).withRel("cidades")); // modo 3
        if (algaSecurity.autenticadoEEscopoLeitura()) {
            cidadeModel.add(algaLinks.linkToCidades("cidades"));
            cidadeModel.getEstado().add(algaLinks.linkToEstado(cidadeModel.getEstado().getId()));
        }
        //cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class).buscar(cidadeModel.getEstado().getId())).withSelfRel());
        return cidadeModel;
    }

    @Override
    public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities){
        CollectionModel<CidadeModel> collectionModel = super.toCollectionModel(entities);
        if (algaSecurity.autenticadoEEscopoLeitura()) {
            collectionModel.add(algaLinks.linkToCidades());
        }
        return collectionModel;
    }

    // metodo vem automaticamente da classe pai RepresentationModelAssemblerSupport
//    public List<CidadeModel> toCollectionModel(List<Cidade> cidades){
//        return cidades.stream()
//                .map(cidade -> toModel(cidade))
//                .collect(Collectors.toList());
//    }
}
