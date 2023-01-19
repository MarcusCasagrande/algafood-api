package com.algaworks.algafood.core.security.authorizationserver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// classe pra pegar as conf da chave do jwt (23.10)
@Getter
@Setter
@Validated
@Component
@ConfigurationProperties("algafood.jwt.keystore")
public class JwtKeyStoreProperties {

    @NotNull // 23.41: mudando de tipo String para Resource, nao entendi direito pq
    private Resource jksLocation;

    @NotBlank
    private String password;

    @NotBlank
    private String keypairAlias;
}
