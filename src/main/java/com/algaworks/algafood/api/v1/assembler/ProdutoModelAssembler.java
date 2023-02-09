package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteProdutoController;
import com.algaworks.algafood.api.v1.model.objectmodel.ProdutoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ProdutoModelAssembler extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public ProdutoModelAssembler() {
        super(RestauranteProdutoController.class, ProdutoModel.class);
    }

    @Override
    public ProdutoModel toModel(Produto produto) {
        ProdutoModel produtoModel = createModelWithId(produto.getId(), produto, produto.getRestaurante().getId()); // terceiro param na aula (19.31)
        modelMapper.map(produto, produtoModel);
        if (algaSecurity.autenticadoEEscopoLeitura()) {
            produtoModel.add(algaLinks.linkToProdutos(produto.getRestaurante().getId(), "produtoss"));
            produtoModel.add(algaLinks.linkToFoto(produto.getRestaurante().getId(), produto.getId(), "fotoAdded"));
        }

        return produtoModel;
    }
}
