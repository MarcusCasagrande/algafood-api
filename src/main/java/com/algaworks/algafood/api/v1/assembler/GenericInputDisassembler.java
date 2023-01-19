package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A generic Input disassembler that uses Model Mapper.
 *
 * @param <T> Input to disassemble from.
 * @param <S> Domain to disassemble to.
 */
@Component
public class GenericInputDisassembler<T, S> {

    /*
    Class<S> objectModelType = (Class<S>)
            ((ParameterizedType)getClass().getGenericSuperclass())
                    .getActualTypeArguments()[1];
      */

    @Autowired
    private ModelMapper modelMapper;

    public S toDomainModel(T originInput, Class<S> object) {
        return modelMapper.map(originInput, object);
    }

    public void copyToDomainObject(T originInput, Object destination) {
        modelMapper.map(originInput, destination);
    }
}
