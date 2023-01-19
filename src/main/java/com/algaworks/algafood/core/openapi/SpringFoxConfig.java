package com.algaworks.algafood.core.openapi;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.model.objectmodel.*;
import com.algaworks.algafood.api.v1.openapi.model.*;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.openapi.model.CidadesModelOpenApiV2;
import com.algaworks.algafood.api.v2.openapi.model.CozinhasModelOpenApiV2;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Configuration
//@EnableWebMvc // incluir, pois: https://stackoverflow.com/questions/70178343/springfox-3-0-0-is-not-working-with-spring-boot-2-6-0 (NAO INCLUIR, pois faz todos response virar XML por padrao ao inves JSON)
@Import(BeanValidatorPluginsConfiguration.class) // pro springfox usar as validacoes dos bean (@NotNull) pra confirmar sua obrigatoriedade no SwaggerUi
public class SpringFoxConfig {

    @Bean // Bean apenas pra fazer com que o SwaggerUI reconheca o tipo OffsetDateTime (ver aula 18.14). Requeriu nova dependencia "jsr310" no pom
    public JacksonModuleRegistrar springFoxJacksonConfig() {
        return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
    }

    @Bean // se comentar, nao carrega o Bean e a documentacao nao vai ter essa versao (20.19)
    public Docket apiDocketV1(){
        TypeResolver typeResolver = new TypeResolver();
        return new Docket(DocumentationType.OAS_30)
                .groupName("V1") // swagger topo direito "Select a spec"
                .select() //builder
//                  .apis(RequestHandlerSelectors.any()) // qualquer controlador que encontrar no projeto relacionado a api, pode documentar
                    .apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api")) // somente controladores do nosso pacote (os de error so spring nao vem)
                  .paths(PathSelectors.ant("/v1/**")) // 20.16: documentacao mostra somente o caminho que comecar com esse prefixo
                    .build()
                .useDefaultResponseMessages(false) // status que nao implementamos, nao aparecem (como 401, 403)
                .globalResponses(HttpMethod.GET, globalGetResponseMessages()) // todos os métodos get terão essa lista de status retornaveis no SwaggerUI
                .globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
                .globalResponses(HttpMethod.PUT , globalPostPutResponseMessages())
                .globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
//                .globalRequestParameters(Collections.singletonList( // IMPORTANTE PRA SETAR UM PARAMETRO GLOBAL PRA TODOS OS METODOS
//                        new RequestParameterBuilder() // adiciona um parametro implicito em todos os endpoints, que seria o "campos"
//                                .name("campos")
//                                .description("Nomes das propriedades para filtrar na resposta, separados por vírgula")
//                                .in(ParameterType.QUERY) // tipo query indica que é um tipo que vem como questystring na url
//                                .required(true)
//                                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING))) // modelo simples tipo string
//                                .build())
//                )

                .additionalModels(typeResolver.resolve(Problem.class)) // pro SwaggerUI carregar o modelo de retorno do Problem na lista de Schemas
                .ignoredParameterTypes(ServletWebRequest.class, URL.class, URI.class, URLStreamHandler.class, Resource.class, File.class, InputStream.class) // 18.23 (ignora os paramentros desse tipo dos metodos de controller). Tod-o o resto veio da aula 18.26
                .directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
                .directModelSubstitute(Links.class, LinksModelOpenApi.class) //(19.39) (essa linha apenas)
                .alternateTypeRules(AlternateTypeRules.newRule( // substitui um Page<CozinhaModel> para um CozinhasModelOpenApi. (método listar() de CozinhaController. Aula 18.21)
                        typeResolver.resolve(PagedModel.class, CozinhaModel.class), CozinhasModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(Page.class, PedidoResumoModel.class), PedidoResumoModelOpenApi.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, CidadeModel.class), CidadesModelOpenApi.class)) //(19.40, corrigindo links)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, EstadoModel.class), EstadosModelOpenApi.class)) //(19.42, desafio igual o acima)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, FormaPagamentoModel.class), FormasPagamentoModelOpenApi.class)) //(19.43, desafio)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, GrupoModel.class), GruposModelOpenApi.class)) //(19.44, desafio)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, PermissaoModel.class), PermissoesModelOpenApi.class)) //(19.44, desafio)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(PagedModel.class, PedidoResumoModel.class), PedidosModelOpenApi.class)) //(19.45, desafio)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, RestauranteBasicoModel.class), RestaurantesModelOpenApi.class)) //(19.47, desafio)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, UsuarioModel.class), UsuariosModelOpenApi.class)) //(19.47, desafio)
                .apiInfo(apiInfoV1())
                .tags(
                        new Tag("Cidades", "Gerencia as cidades"),  // ver CidadeController interface
                        new Tag("Grupos", "gerencia os grupos de usuários"),
                        new Tag("Cozinhaas", "Gerencia as cozinhas"),
                        new Tag("Formas de Pagto TAG", "Gerencia as formas de pagamento"),
                        new Tag("Peedidos", "Mostra todos os pedidos copm base nos filtros dados"),
                        new Tag("Restaurantes", "Gerencia os restaurantes"),
                        new Tag("Produtos", "Gerencia os produtos de restaurantes"),
                        new Tag("Usuarios", "Gerencia os userss"),
                        new Tag("Estatísticas", "Estatísticas da AlgaFood")
                )
               //.securitySchemes(Arrays.asList(securityScheme())); // 23.42
                // solucao jwt springfox3:
                .securityContexts(Arrays.asList(securityContext())) // configura que todos os endpoints tem que ter autorizacao
                .securitySchemes(List.of(authenticationScheme()))
                .securityContexts(List.of(securityContext()));
    }

    private SecurityScheme securityScheme(){ //23.42
        return new OAuthBuilder()
                .name("Algafood")
                .grantTypes(grantTypes()) // especifica uri pra gerar token
                .scopes(scopes()) // define os escopos
                .build();
    }

    private List<GrantType> grantTypes(){
        return Arrays.asList(new ResourceOwnerPasswordCredentialsGrant("/oauth/token")); // vamos usar o password flow
    }

    private List<AuthorizationScope> scopes(){
        return Arrays.asList(new AuthorizationScope("READ", "Acesso de leitura"),
                             new AuthorizationScope("WRITE", "Acesso de escrita"));
    }

    /**
    ADICIONANDO 3 METODOS REFERENTES A SOLUCAO JWT DO SPRINGFOX (23.42
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(securityReference()).build();
    }
    private List<SecurityReference> securityReference() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("Authorization", authorizationScopes));
    }
    private HttpAuthenticationScheme authenticationScheme() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("Authorization").build();
    }

    //@Bean // se comentar, nao carrega o Bean e a documentacao nao vai ter essa versao (20.19)
    public Docket apiDocketV2(){
        TypeResolver typeResolver = new TypeResolver();
        return new Docket(DocumentationType.OAS_30)
                .groupName("V2")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api")) // somente controladores do nosso pacote (os de error so spring nao vem)
                .paths(PathSelectors.ant("/v2/**"))
                .build()
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, globalGetResponseMessages()) // todos os métodos get terão essa lista de status retornaveis no SwaggerUI
                .globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
                .globalResponses(HttpMethod.PUT , globalPostPutResponseMessages())
                .globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
                .additionalModels(typeResolver.resolve(Problem.class)) // pro SwaggerUI carregar o modelo de retorno do Problem na lista de Schemas
                .ignoredParameterTypes(ServletWebRequest.class, URL.class, URI.class, URLStreamHandler.class, Resource.class, File.class, InputStream.class) // 18.23 (ignora os paramentros desse tipo dos metodos de controller). Tod-o o resto veio da aula 18.26
                .directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
                .directModelSubstitute(Links.class, LinksModelOpenApi.class) //(19.39) (essa linha apenas)
                .alternateTypeRules(AlternateTypeRules.newRule( // substitui um Page<CozinhaModel> para um CozinhasModelOpenApi. (método listar() de CozinhaController. Aula 18.21)
                        typeResolver.resolve(PagedModel.class, CozinhaModelV2.class), CozinhasModelOpenApiV2.class))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(CollectionModel.class, CidadeModelV2.class), CidadesModelOpenApiV2.class)) //(19.40, corrigindo links)
                .apiInfo(apiInfoV2())
                .tags(
                        new Tag("Cidades", "Gerencia as cidades (V2)"),
                        new Tag("Cozinhaas", "Gerencia as cozinhas (V2)")
                );

    }

    private List<Response> globalGetResponseMessages(){ // 200 nao vai, pois o springfox pega automaticamente por padrao pela descricao do método no controller. Mesmo se for inserido aqui, substitui por um padrao json)
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno do Servidor")
                        .representation(MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference()) // aula 18.15
                        .build(),
                new ResponseBuilder() // NOT_ACCEPTABLE nao tem corpo
                        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value())) // pra quando o front-end quer XML, mas só enviamos JSON
                        .description("Recurso não possui representação que pode ser aceita pelo consumidor")
                        .build()
        );
    }

    private List<Response> globalPostPutResponseMessages(){
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno do Servidor")
                        .representation( MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference()) // aula 18.15
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description("Requisição inválida")
                        .representation( MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference()) // aula 18.15
                        .build(),
                new ResponseBuilder()
                    .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
                    .description("Recurso não possui representação que pode ser aceita pelo consumidor")
                    .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
                        .description("Requisição recusada porque o corpo está em um formato não suportado")
                        .representation( MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference()) // aula 18.15
                        .build()
        );
    }

    private List<Response> globalDeleteResponseMessages(){
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno do Servidor")
                        .representation( MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference()) // aula 18.15
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description("Código inserido não está cadastrado no sistema")
                        .representation( MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference()) // aula 18.15
                        .build()
        );
    }

    // trocar titulo, descricao e outras firulas da pageina do swaggerUI
    private ApiInfo apiInfoV1() {
        return new ApiInfoBuilder()
                .title("AlgaFood API")
                .description("API aberta para clientes e restaurantes,<br>" +
                        "<strong> Essa versao da API deixara de existir lala")
                .version("1")
                .contact(new Contact("AlgaWorks", "http://www.algaworks.com", "contato@algaworks.com"))
                .build();
    }

    private ApiInfo apiInfoV2() {
        return new ApiInfoBuilder()
                .title("AlgaFood API (V2)")
                .description("API aberta para clientes e restaurantes (V2)")
                .version("2")
                .contact(new Contact("AlgaWorks", "http://www.algaworks.com", "contato@algaworks.com"))
                .build();
    }

    // método que busca uma referencia de Problem. É um work around das deficiencias do SpringFox 3 pra poder fazer com que os codigos de status possam mostrar uma representacao de Problem no SwaggerUi (ver aula 18.15)
    private Consumer<RepresentationBuilder> getProblemaModelReference() {
        return r -> r.model(m -> m.name("Problema")
                .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
                        q -> q.name("Problema").namespace("com.algaworks.algafood.api.exceptionhandler")))));
    }

    //mudar

//      Para versao anterior com SwaggerUI. Deixei aqui pra refrencia de como buscar classpath. (Ver aula 18.4)
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**") // 2 asterisco significa procura tudo que tem pela frente incluindo pastas e subpastas
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

}
