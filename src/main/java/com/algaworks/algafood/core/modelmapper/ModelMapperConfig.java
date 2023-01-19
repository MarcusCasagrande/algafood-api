package com.algaworks.algafood.core.modelmapper;

import com.algaworks.algafood.api.v1.model.input.ItemPedidoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.EnderecoModel;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        var modelMapper = new ModelMapper();
//        modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
//                .addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);

        //ItemPedidoInput.produtoId vai ignorar sua atribuicao em ItemPedido.id
        modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
                .addMappings(mapper -> mapper.skip(ItemPedido::setId));

        var enderecoToEnderecoModelTypemap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
        enderecoToEnderecoModelTypemap.<String>addMapping(
                enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(),
                (enderecoModeldest, value) -> enderecoModeldest.getCidade().setEstado(value));

        modelMapper.createTypeMap(CidadeInputV2.class, Cidade.class) // pular o "nomeEstado" sendo setado em id da cidade,no mapper do POST de CidadeControllerV2 (20.11)
                .addMappings(mapper -> mapper.skip(Cidade::setId));

        return modelMapper;
    }
}
