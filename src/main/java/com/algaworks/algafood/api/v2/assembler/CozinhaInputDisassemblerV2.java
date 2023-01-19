package com.algaworks.algafood.api.v2.assembler;

import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CozinhaInputDisassemblerV2 {

    @Autowired // classe de ModelMapperconfig de @Config instancia isso
    private ModelMapper modelMapper;

    public Cozinha toDomainModel(CozinhaInputV2 cozinhaInput){
        return modelMapper.map(cozinhaInput, Cozinha.class);
    }

    public void copyToDomainObject(CozinhaInputV2 cozinhaInput, Cozinha cozinha){
        modelMapper.map(cozinhaInput, cozinha);
    }
}
