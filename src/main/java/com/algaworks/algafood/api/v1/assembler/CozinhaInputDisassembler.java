package com.algaworks.algafood.api.v1.assembler;

import com.algaworks.algafood.api.v1.model.input.CozinhaInput;
import com.algaworks.algafood.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CozinhaInputDisassembler {

    @Autowired // classe de ModelMapperconfig de @Config instancia isso
    private ModelMapper modelMapper;

    public Cozinha toDomainModel(CozinhaInput cozinhaInput){
        return modelMapper.map(cozinhaInput, Cozinha.class);
    }

    public void copyToDomainObject(CozinhaInput cozinhaInput, Cozinha cozinha){
        modelMapper.map(cozinhaInput, cozinha);
    }
}
