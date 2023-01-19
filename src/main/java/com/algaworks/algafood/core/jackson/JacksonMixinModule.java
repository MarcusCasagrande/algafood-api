package com.algaworks.algafood.core.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class JacksonMixinModule extends SimpleModule {

    public JacksonMixinModule(){
        //setMixInAnnotation(Cidade.class, CidadeMixin.class);
        //setMixInAnnotation(Cozinha.class, CozinhaMixin.class);
        //setMixInAnnotation(Pedido.class, PedidoMixin.class);
        //setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
        //setMixInAnnotation(Usuario.class, UsuarioMixin.class);
    }
}
