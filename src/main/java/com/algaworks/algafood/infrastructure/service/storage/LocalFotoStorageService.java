package com.algaworks.algafood.infrastructure.service.storage;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.nio.file.Files;
import java.nio.file.Path;

//@Service // comentado, pois vai ser beanizado de outra forma
public class LocalFotoStorageService implements FotoStorageService {

    //@Value("${algafood.storage.local.diretorio-fotos}")
    //private Path diretorioFotos;

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public FotoRecuperada recuperar(String nomeArquivo) {
        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);
            FotoRecuperada fotoRecuperada = FotoRecuperada.builder()
                    .inputStream(Files.newInputStream(arquivoPath))
                    .build();

            return fotoRecuperada;
        } catch (Exception e) {
            throw new StorageException("Não foi possível obter o arquivo", e);
        }
    }

    @Override // nao recebo um MultipartFile pra evitar acoplar classes de web aqui no infrastructure, mesmo que fosse mais facil
    public void armazenar(NovaFoto novaFoto) {
        try {
            Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        } catch (Exception e) {
            throw new StorageException("Não foi possível armazenar arquivo.", e);
        }
    }

    @Override
    public void remover(String nomeArquivo) {
        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);
            Files.deleteIfExists(arquivoPath);
        } catch (Exception e) {
            throw new StorageException("Não foi possível excluir arquivo", e);
        }
    }

    private Path getArquivoPath(String nomeArquivo){
        //return diretorioFotos.resolve(Path.of(nomeArquivo));
        return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
    }
}
