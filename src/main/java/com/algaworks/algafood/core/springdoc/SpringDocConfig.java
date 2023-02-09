package com.algaworks.algafood.core.springdoc;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// 26.3 (classe criada)
@Configuration
// 26.5: pra autorizar quando mexer no endpoint da documentacao
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(
        authorizationCode = @OAuthFlow(authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}", //nomes custom, nao sao padrao
                                        tokenUrl = "${springdoc.oAuthFlow.tokenUrl}", //custom tbm
                                        scopes = {
                                            @OAuthScope(name = "READ", description = "read scope"),
                                            @OAuthScope(name = "WRITE", description = "write scope")
                                        }
        )))
public class SpringDocConfig {

    private static final String BadRequestResponse = "BadRequestResponse";
    private static final String NotFoundResponse = "NotFoundResponse";
    private static final String NotAcceptableResponse = "NotAcceptableResponse";
    private static final String InternalServerError = "InternalServerError";

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info().title("AlgaFood API (title)")
                    .version("v1")
                    .description("RESR API do Algafood")
                    .license(new License()
                            .name("Apache 2.0")
                            .url("http://springdoc.com")
                    )
                ).externalDocs(new ExternalDocumentation()
                        .description("Algaworks (description)")
                        .url("https://algaworks.com")
                ).tags(Arrays.asList( //26.6
                        new Tag().name("Cidades").description("Gerencia cidades (via conf)"), // nao é prioridade sobre o especifico
                        new Tag().name("Grupos").description("Gerencia os grupos")
                )).components(new Components()
                        .schemas(gerarSchemas())
                        .responses(gerarResponses()) //26.15
                ); // 26.14
    }

    //26.14: método pra gerar schemas (classe Problem por exemplo)
    private Map<String, Schema> gerarSchemas(){
        final Map<String, Schema> schemaMap = new HashMap<>();
        Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
        Map<String, Schema> problemObjectSchema = ModelConverters.getInstance().read(Problem.Objects.class);
        schemaMap.putAll(problemSchema);
        schemaMap.putAll(problemObjectSchema);
        return schemaMap;
    }
    private Map<String, ApiResponse> gerarResponses() { //26.15
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();
        Content content = new Content().addMediaType(APPLICATION_JSON_VALUE, new MediaType().schema(new Schema<Problem>().$ref("Problema"))); // atencao no import do mediatype. Tem 2 nessa linha
        apiResponseMap.put(BadRequestResponse, new ApiResponse().description("requisição inválida").content(content));
        apiResponseMap.put(NotFoundResponse, new ApiResponse().description("Recurso não encontrado").content(content));
        apiResponseMap.put(NotAcceptableResponse, new ApiResponse().description("Recurso não possui representação que poderia ser aceita pelo consumidor").content(content));
        apiResponseMap.put(InternalServerError, new ApiResponse().description("Erro interno no servidor").content(content));
        return apiResponseMap;
    }

    @Bean // 26.13
    public OpenApiCustomiser openApiCustomiser(){
        return openApi -> {
            openApi.getPaths()
                    .values()
                    .forEach(pathItem -> pathItem.readOperationsMap()
                            .forEach((httpMethod, operation) -> {
                                ApiResponses responses = operation.getResponses();
                                switch (httpMethod){ // 404 comentado/removido para que saia do escopo global (que é prioridade) e possa ser tratado individualmente nos end-points
                                    case GET:
                                       // responses.addApiResponse("404", new ApiResponse().$ref(NotFoundResponse));
                                        responses.addApiResponse("406", new ApiResponse().$ref(NotAcceptableResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(InternalServerError));
                                        break;
                                    case POST:
                                        responses.addApiResponse("400", new ApiResponse().$ref(BadRequestResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(InternalServerError));
                                        break;
                                    case PUT:
                                        responses.addApiResponse("400", new ApiResponse().$ref(BadRequestResponse)); //26.15: esses refs vem do metodo privado gerarResponses(). Não entendi como.
                                        //responses.addApiResponse("404", new ApiResponse().$ref(NotFoundResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(InternalServerError));
                                        break;
                                    case DELETE:
                                        //responses.addApiResponse("404", new ApiResponse().$ref(NotFoundResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(InternalServerError));
                                        break;
                                    default:
                                        responses.addApiResponse("500", new ApiResponse().$ref(InternalServerError));
                                        break;
                                }

                            })

                    );
        };
    }

    /**
     * NAO VAI SER USADO. FOI SÓ EXEMPLO

    @Bean //26.11: classe pra customizar respostas do documento OpenApi
    public OpenApiCustomiser openApiCustomiser(){
        return openApi -> {
            openApi.getPaths()
                    .values()
                    .stream()
                    .flatMap(pathItem -> pathItem.readOperations().stream())
                    .forEach(operation -> {
                        ApiResponses responses = operation.getResponses();
                        ApiResponse apiResponseNaoEncontrado = new ApiResponse().description("Recurso não encontrado");
                        ApiResponse apiResponseErroInterno = new ApiResponse().description("Erro interno no servidor");
                        ApiResponse apiResponseSemrepresentacao = new ApiResponse().description("Recurso não possui uma representação que podeira ser aceita pelo consumidor");
                        responses.addApiResponse("404", apiResponseNaoEncontrado);
                        responses.addApiResponse("406", apiResponseSemrepresentacao);
                        responses.addApiResponse("500", apiResponseErroInterno);

                    });
        };
    }

    @Bean // 26.4: fazer custom documentation, mais de um tipo de documentacoa pro mesmo projeto. (essa classe implementa um tipo de um grupo de documentacao, ao inves da classe acima que é somente uma)
    public GroupedOpenApi groupedOpenApi(){
        return GroupedOpenApi.builder()
                .group("Algafood API admin (grouped class)")
                .pathsToMatch("/v1/**")
                .addOpenApiCustomiser(openApi -> {
                    openApi.info(new Info().title("AlgaFood API (title)")
                            .version("v1")
                            .description("RESR API do Algafood")
                            .license(new License()
                                    .name("Apache 2.0")
                                    .url("http://springdoc.com")
                            )
                    ).externalDocs(new ExternalDocumentation()
                            .description("Algaworks (description)")
                            .url("https://algaworks.com")
                    );
                })
                .build();
    }

    @Bean
    public GroupedOpenApi groupedOpenApiCliente(){
        return GroupedOpenApi.builder()
                .group("Algafood API (grouped class client)")
                .pathsToMatch("/cliente/v1/**")
                .addOpenApiCustomiser(openApi -> {
                    openApi.info(new Info().title("AlgaFood API (title)")
                            .version("v1")
                            .description("REST API do cliente")
                            .license(new License()
                                    .name("Apache 2.0")
                                    .url("http://springdoc.com")
                            )
                    ).externalDocs(new ExternalDocumentation()
                            .description("Algaworks (description)")
                            .url("https://algaworks.com")
                    );
                })
                .build();
    }
    */
}
