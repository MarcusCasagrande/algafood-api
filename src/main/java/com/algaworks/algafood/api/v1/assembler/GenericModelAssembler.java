package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A generic Model Assembler that uses ModelMapper.
 *
 * @param <T> Model type to assemble from.
 * @param <S> Domain type to assemble to.
 */
@Component
public class GenericModelAssembler<T, S> {

//    private ModelMapper modelMapper;
//    public GenericModelAssembler(ModelMapper modelMapper) {
//        this.modelMapper = modelMapper;
//    }

    @Autowired
    private ModelMapper modelMapper;

    public S toModel(T domainObject, Class<S> objectModelType){
        return modelMapper.map(domainObject, objectModelType);
    }

    public List<S> toCollectionModel(Collection<T> objects, Class<S> objectModelType) {
        return objects.stream()
                .map(object -> toModel(object, objectModelType))
                .collect(Collectors.toList());
    }
}
