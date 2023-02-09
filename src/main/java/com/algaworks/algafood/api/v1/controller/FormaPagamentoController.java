package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.GenericInputDisassembler;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInput;
import com.algaworks.algafood.api.v1.model.objectmodel.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.openapi.controller.FormaPagamentoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/v1/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {

    @Autowired
    FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    CadastroFormaPagamentoService cadastroFormaPagamentoService;

//    @Autowired
//    GenericModelAssembler<FormaPagamento, FormaPagamentoModel> FMA;

    @Autowired
    FormaPagamentoModelAssembler FPMA;

    @Autowired
    GenericInputDisassembler<FormaPagamentoInput, FormaPagamento> FID;

    @CheckSecurity.FormasPagamento.PodeConsultar
    @GetMapping
    public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request){ //injetado aqui no metodo aparentemente, pra ser usado no shallow abaixo e dar disable
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest()); // desabilitada o shallow Etag pra essa requisicao
        String eTag = "0";
        OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao(); //cria-se o atributto Offsetdatetime no model de formaPagamento pra pegar esse atributo e usar pra calcular se teve modificacao no etag e atualizar o cache. ver capitulos referentes
        if (dataUltimaAtualizacao != null){
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond()); // number of seconds since 1970
        }
        if (request.checkNotModified(eTag)){ // <-- esse metodo checkNotModified também ja seta o response pra 304
            return null;
        }

        CollectionModel<FormaPagamentoModel> formasPagamento =  FPMA.toCollectionModel(formaPagamentoRepository.findAll());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS)) // podia ser feito via header tambem
                .eTag(eTag)
//              .header("ETag", eTag)
                .body(formasPagamento);
    }

    @CheckSecurity.FormasPagamento.PodeConsultar
    @GetMapping(value = "/{id}")
    public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long id, ServletWebRequest request){
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        String eTag = "0";
        OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataAtualizacaoById(id);
        if (dataUltimaAtualizacao != null){
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond()); // number of seconds since 1970
        }
        if (request.checkNotModified(eTag)){ // <-- esse metodo checkNotModified também ja seta o response pra 304
            return null;
        }
        FormaPagamentoModel fpm =  FPMA.toModel(cadastroFormaPagamentoService.buscarOuFalhar(id));
        return ResponseEntity.ok()
                // podia ser feito via header tambem:
              //.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
              //.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate()) // cachePrivate significa que só pode ser armazenado em cache local do navegador, nao em caches compartilhados intermediarios de proxy. Public é padrão armazenamento
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
              //.cacheControl(CacheControl.noCache()) // nao quer dizer que nao pode fazer cache. Significa que o cache sempre está em stale, sempre precisa fazer validacao, significa que o maxage=0
              //.cacheControl(CacheControl.noStore()) // agora sim, sem cache
                .eTag(eTag)
                .body(fpm);
        // No navegador, ele pode incluir na request o header: Cache-Control : no-cache. Que vai ignorar o cache e vir direto no servidor buscar a info
    }

    @CheckSecurity.FormasPagamento.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput){
        FormaPagamento fp = FID.toDomainModel(formaPagamentoInput, FormaPagamento.class);
        return FPMA.toModel(cadastroFormaPagamentoService.salvar(fp));
    }

    @CheckSecurity.FormasPagamento.PodeEditar
    @PutMapping(value = "/{id}")
    public FormaPagamentoModel atualizar(@PathVariable long id, @RequestBody @Valid FormaPagamentoInput formaPagamentoInput){
        FormaPagamento fp = cadastroFormaPagamentoService.buscarOuFalhar(id);
        FID.copyToDomainObject(formaPagamentoInput, fp);
        return FPMA.toModel(cadastroFormaPagamentoService.salvar(fp));
    }

    @CheckSecurity.FormasPagamento.PodeEditar
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id){
        cadastroFormaPagamentoService.excluir(id);
    }





}
