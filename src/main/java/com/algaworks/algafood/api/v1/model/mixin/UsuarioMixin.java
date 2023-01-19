package com.algaworks.algafood.api.v1.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public abstract class UsuarioMixin {

    @JsonIgnore
    private OffsetDateTime dataCadastro;
}
