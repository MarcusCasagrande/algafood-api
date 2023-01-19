package com.algaworks.algafood.api.v1.controller;

// poderia tranquilamente por em ProdutoController ao inves de fazer esse novo controller aqui

import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.FotoProdutoModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import com.algaworks.algafood.api.v1.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.algaworks.algafood.domain.service.FotoStorageService.FotoRecuperada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/restaurantes/{restauranteId}/produtos/{produtoId}/foto", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi {

    @Autowired
    private CatalogoFotoProdutoService catalogoFotoProdutoService;

    @Autowired
    private CadastroProdutoService cadastroProdutoService;

//    @Autowired
//    private GenericModelAssembler<FotoProduto, FotoProdutoModel> GMA;
    @Autowired
    private FotoProdutoModelAssembler GMA;

    @Autowired
    private FotoStorageService fotoStorageService;

    /*
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // parametros da requisicao (form-data) nao requerem @RequestBody
    public void atualizarFoto(@PathVariable Long restauranteId, @PathVariable long produtoId, @Valid FotoProdutoInput fotoProdutoInput){
        var nomeArquivo = UUID.randomUUID().toString() + "_" + fotoProdutoInput.getArquivo().getOriginalFilename();
        var arquivoFoto = Path.of("C:/Users/marcu/Desktop/uploaded", nomeArquivo);
        System.out.println(fotoProdutoInput.getDescricao());
        System.out.println(arquivoFoto);
        System.out.println(fotoProdutoInput.getArquivo().getContentType());
        try {
            fotoProdutoInput.getArquivo().transferTo(arquivoFoto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */

    @GetMapping // se accept for JSON, cai aqui. Se for qualquer outra coisa, cai no metodo "servirFoto"
    @CheckSecurity.Restaurantes.PodeConsultar
    public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable long produtoId){
        FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
        return GMA.toModel(fotoProduto);
    }

//    @CheckSecurity.Restaurantes.PodeConsultar // as fotos dos produtos ficarão públicas (não precisa de autorização para acessá-las)
    @GetMapping(produces = MediaType.ALL_VALUE) // especifrica que retorna qualquer, pois a classe define os metodos retornam JSON
    public ResponseEntity<?> servirFoto(@PathVariable Long restauranteId, @PathVariable long produtoId,
                                                          @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
        try {
            FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType()); // converte string num mediatype
            List<MediaType> mediatypesAceitas = MediaType.parseMediaTypes(acceptHeader); // o front-end pode passar mais de um tipo de accept
            getVerificarCompatibilidadeMediaType(mediaTypeFoto, mediatypesAceitas);
//            InputStream is = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());
            FotoRecuperada fotoRecuperada = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());
            if (fotoRecuperada.temUrl()){
                return ResponseEntity
                        .status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl()) // url vai estar no header da resposta
                        .build();
            } else {
                return ResponseEntity.ok()
                        .contentType(mediaTypeFoto)
                        .body(new InputStreamResource(fotoRecuperada.getInputStream()));
            }


        } catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }
    }

    private static void getVerificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediatypesAceitas) throws HttpMediaTypeNotAcceptableException {
        boolean compativel = mediatypesAceitas.stream()
                .anyMatch(mediaAceita -> mediaAceita.isCompatibleWith(mediaTypeFoto));
        if (!compativel){
            throw new HttpMediaTypeNotAcceptableException(mediatypesAceitas); // essa excecao já é tratada pelo exceptionhandler
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // parametros da requisicao (form-data) nao requerem @RequestBody
    public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable long produtoId, @Valid FotoProdutoInput fotoProdutoInput, @RequestPart(required = true) MultipartFile arquivo) throws IOException {
        Produto produto = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
       // MultipartFile arquivo = fotoProdutoInput.getArquivo(); //comentado aqui, e inserido nos arg do metodo gracas a enviar fotos via swagger (ver aula 18.36)
        FotoProduto foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setDescricao(fotoProdutoInput.getDescricao());
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(arquivo.getOriginalFilename());

        System.out.println(arquivo.getOriginalFilename());
        System.out.println(arquivo.getName());

        //foto.setId(produto.getId());
        return GMA.toModel(catalogoFotoProdutoService.salvar(foto, arquivo.getInputStream()));
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @DeleteMapping
    public void deletarfoto(@PathVariable Long restauranteId, @PathVariable long produtoId){
        catalogoFotoProdutoService.deletar(restauranteId, produtoId);

    }

}
