package com.algaworks.algafood.api.v2.assembler;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.CidadeController;
import com.algaworks.algafood.api.v1.model.objectmodel.CidadeModel;
import com.algaworks.algafood.api.v2.AlgaLinksV2;
import com.algaworks.algafood.api.v2.controller.CidadeControllerV2;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CidadeModelAssemblerV2 extends RepresentationModelAssemblerSupport<Cidade, CidadeModelV2> {


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinksV2 algaLinks;

    public CidadeModelAssemblerV2(){
        super(CidadeControllerV2.class, CidadeModelV2.class);
    }

    @Override
    public CidadeModelV2 toModel(Cidade cidade) {
        CidadeModelV2 cidadeModel = createModelWithId(cidade.getId(), cidade); // essas 2 linhas equivalem as 3 debaixo comentadas)
        modelMapper.map(cidade, cidadeModel);
        cidadeModel.add(algaLinks.linkToCidades("cidades"));

        return cidadeModel;
    }

    @Override
    public CollectionModel<CidadeModelV2> toCollectionModel(Iterable<? extends Cidade> entities){
        return super.toCollectionModel(entities)
                .add(algaLinks.linkToCidades());
    }

    // metodo vem automaticamente da classe pai RepresentationModelAssemblerSupport
//    public List<CidadeModel> toCollectionModel(List<Cidade> cidades){
//        return cidades.stream()
//                .map(cidade -> toModel(cidade))
//                .collect(Collectors.toList());
//    }
}
