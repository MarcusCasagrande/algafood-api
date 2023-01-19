package com.algaworks.algafood.core.storage;

import com.amazonaws.regions.Regions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Getter
@Setter
@Component
@ConfigurationProperties("algafood.storage")
public class StorageProperties {

    private Local local = new Local();
    private S3 s3 = new S3();
    private TipoStorage tipo = TipoStorage.LOCAL; // definindo default

    public enum TipoStorage{
        LOCAL, S3
    }

    @Getter
    @Setter
    public class Local {
        private Path diretorioFotos; // ja virou o "fully qualified name" da prop "algafood.storage.local.diretorio-fotos"
    }

    @Getter
    @Setter
    public class S3 {
        private String idChaveAcesso;
        private String chaveAcessoSecreta;
        private String bucket;
        private Regions regiao;
        private String diretorioFotos;
        /*
        algafood.storage.s3.id-chave-acesso=AKIASZLJ5IJRIIBWCONC
        algafood.storage.s3.chave-acesso-secreta=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
        algafood.storage.s3.bucket=marcus-algafood-teste
        algafood.storage.s3.regiao=us-east-1
        algafood.storage.s3.diretorio-fotos=catalogo
        */
    }


}
