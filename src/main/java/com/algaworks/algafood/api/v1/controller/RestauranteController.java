package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.v1.model.input.RestauranteInput;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteIdNomeModel;
import com.algaworks.algafood.api.v1.model.objectmodel.RestauranteModel;
import com.algaworks.algafood.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.core.storage.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

//(origins = "http://localhost") CROSS ORIGIN TIRADO DAQUI PRA SER SETADO GLOBALMENTE
//@CrossOrigin(maxAge = 10) // max age define o tempo maximo que o browse pode armazenar em cache o preflight (OPTIONS), aula 16.5
// se fizer crossOrigin, e a requisicao nao for simples (nao for get, head, e aparentemente post tbm), o browser faz o tal preflight
// aqui tme tudo https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS#simple_requests
@RestController // (já inclui @Controller e @ResponseBody)
@RequestMapping(value = "/v1/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenApi {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired //serve para fazer validacao do objeto. Usado no metodo patch aqui
    private SmartValidator validator;

    @Autowired
    private RestauranteModelAssembler RMA;

    @Autowired
    private RestauranteBasicoModelAssembler RBMA;

    @Autowired
    private RestauranteApenasNomeModelAssembler RANMA;

    @Autowired
    private RestauranteInputDisassembler RID;

//    @GetMapping
//    public MappingJacksonValue listar(@RequestParam(required = false) String projecao){ // retorno que terá classe envelopada dependendo do view usado
//        List<Restaurante> restaurantes = restauranteRepository.findAll();
//        List<RestauranteModel> restaurantesModel = RMA.toCollectionModel(restaurantes);
//        MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restaurantesModel);
//
//        if("apenas-nome".equals(projecao)){
//            restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
//        } else if ("completo".equals(projecao)){
//            restaurantesWrapper.setSerializationView(null);
//        } else {
//            restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);
//        }
//        return restaurantesWrapper;
//    }

    //@JsonView(RestauranteView.Resumo.class) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    @GetMapping
    @CheckSecurity.Restaurantes.PodeConsultar
    public CollectionModel<RestauranteBasicoModel> listar(){
        return RBMA.toCollectionModel(restauranteRepository.findAll());
    }

    // MESMO METODO ACIMA, MAS TRABALHANDO COM CORS
//    @GetMapping
//    @JsonView(RestauranteView.Resumo.class)
//    public ResponseEntity<List<RestauranteModel>> listar(){ // ResponseEntity re-inserido pela aula 16.3: CORS
//        List<RestauranteModel> restauranteModel = RMA.toCollectionModel(restauranteRepository.findAll());
//        return ResponseEntity.ok()
//                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost")
//                //.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*") // libera geral
//                .body(restauranteModel);
//    }

//    @JsonView(RestauranteView.Resumo.class)
//    @GetMapping(params = "projecao=resumo")
//    public List<RestauranteModel> listarResumido(){
//        return listar();
//    }
//

   // @JsonView(RestauranteView.ApenasNome.class) //JSONVIEW comentado para poder usar links com o CollectionModel (HATEOAS, aula 19.24)
    @GetMapping(params = "projecao=apenas-nome")
    @CheckSecurity.Restaurantes.PodeConsultar
    public CollectionModel<RestauranteIdNomeModel> listarApenasNomes(){
        return RANMA.toCollectionModel(restauranteRepository.findAll());
    }

    @GetMapping(value = "/{id}")
    @CheckSecurity.Restaurantes.PodeConsultar
    public RestauranteModel buscar(@PathVariable Long id) {
        Restaurante restaurante =  cadastroRestauranteService.buscarOuFalhar(id);
        return RMA.toModel(restaurante);
    }

    @PostMapping
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.CREATED) // substitui o ResponseEnteity<Restaurante> // @Validated(Groups.CozinhaId.class) pra especificar grupo de validacao
    public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput){ // wildcard aceita tanto Restaurante quanto o string do catch
        try {
            Restaurante r = RID.toDomainModel(restauranteInput);
            return RMA.toModel(cadastroRestauranteService.salvar(r));
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e){
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public RestauranteModel atualizar(@PathVariable long id, @RequestBody @Valid RestauranteInput restauranteInput){
        //Restaurante restaurante = RID.toDomainModel(restauranteInput);
        Restaurante r = cadastroRestauranteService.buscarOuFalhar(id);
        RID.copyToDomainObject(restauranteInput, r);
       // BeanUtils.copyProperties(restaurante, r, "id", "formasPagamento", "endereco", "dataCadastro", "produtos"); // dataAtualizacao nao precisa ignorar pois o campo tem a propriedade @UpdateTimestamp do hibernate que ja faz o update time automaticamente
        try {
            return RMA.toModel(cadastroRestauranteService.salvar(r));
        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e){
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public ResponseEntity<Void> ativar(@PathVariable long id){
        cadastroRestauranteService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public ResponseEntity<Void> inativar(@PathVariable long id){
        cadastroRestauranteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/fechamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    public ResponseEntity<Void> fechamento(@PathVariable long id){
        cadastroRestauranteService.fechar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{restauranteId}/abertura")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    public ResponseEntity<Void> abertura(@PathVariable long restauranteId){
        cadastroRestauranteService.abrir(restauranteId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable long id){
        cadastroRestauranteService.excluir(id);
    }

    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public void ativarMultiplos(@RequestBody List<Long> restauranteIds){
        try {
            cadastroRestauranteService.ativar(restauranteIds);
        } catch (RestauranteNaoEncontradoException e){
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public void inativarMultiplos(@RequestBody List<Long> restauranteIds){
        try {
            cadastroRestauranteService.inativar(restauranteIds);
        } catch (RestauranteNaoEncontradoException e){
            throw new NegocioException(e.getMessage(), e);
        }
    }

    /*
    @PatchMapping("/{id}")
    public RestauranteModel atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos, HttpServletRequest request){ // o proprio Spring dá esse campo HttpServletRequest. Aqui chamado apenas para ser usado numa exception no metodo merge
        Restaurante r = cadastroRestauranteService.buscarOuFalhar(id);
        merge(campos, r, request);
        validate(r, "restaurante");
        return atualizar(id, r);
    }
     */

    // metodo pra validar o restaurante que foi "merjado" no patch
    private void validate(Restaurante restaurante, String objName){
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objName);
        validator.validate(restaurante, bindingResult);

        if (bindingResult.hasErrors()){
            throw new ValidacaoException(bindingResult);
        }
    }

    private void merge(Map<String, Object> dadosOrigem, Object objDest, HttpServletRequest request){ //request apenas para ser lancado na excecao ao final, pra nao usar outro deprecated.
        ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true); // para que o patch tambem falhe caso tenha propriedade marcada como @JsonIgnore (ver ApiHandlerException, e campos em app.proprieties)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true); // falhar no patch de requisicoes desconhecidas. Este ja é padrao em patch (ou objectMapper, sei la)
        try {
            Object objOrigin = objectMapper.convertValue(dadosOrigem, objDest.getClass());
            dadosOrigem.forEach((keyName, keyVal) -> {
                Field field = ReflectionUtils.findField(objDest.getClass(), keyName);
                field.setAccessible(true);
                Object newValue = ReflectionUtils.getField(field, objOrigin);
                //System.out.println(keyName + " = " + keyVal + " = " + newValue);
                ReflectionUtils.setField(field, objDest, newValue);
            });
        } catch (IllegalArgumentException e){
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new HttpMessageNotReadableException(e.getMessage(),rootCause, serverHttpRequest); //Um tipo HttpInputMessage.. tudo pra chegar aqui sem usar deprecated
        }

    }
}
