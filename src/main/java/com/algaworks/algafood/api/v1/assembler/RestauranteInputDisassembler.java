package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputDisassembler {

    @Autowired // classe de ModelMapperconfig de @Config instancia isso
    private ModelMapper modelMapper;

    public Restaurante toDomainModel(RestauranteInput restauranteInput){
        /*
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(restauranteInput.getNome());
        restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());

        Cozinha c = new Cozinha();
        c.setId(restauranteInput.getCozinha().getId());
        restaurante.setCozinha(c);
        return restaurante;
         */
        return modelMapper.map(restauranteInput, Restaurante.class);
    }

    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante){
        // no metodo atualizar do RestauranteController, o restaurante (argumento) veio da database (attached). E se modificar o cozinhaId com este metodo, o jpa entende que está tentando modificar o id da cozinha. Ao atribuir um novo cozinha aqui, pega um objeto novo e vazio (desattached) pra preencher com o que quiser pra depois ser inserido denovo na base
        restaurante.setCozinha(new Cozinha());
        if (restaurante.getEndereco() != null){
            restaurante.getEndereco().setCidade(new Cidade());
        }

        modelMapper.map(restauranteInput, restaurante);
    }
}
