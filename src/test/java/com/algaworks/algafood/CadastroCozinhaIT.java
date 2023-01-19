package com.algaworks.algafood;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

// Para que os testes sejam detectados pelo maven-failsafe-plugin, as classes devem terminar com "IT" (nao se usa para testes unitarios)
// para executar os tests agora em cmd, deve-se usar o comando ".\mvnw verify"
//@SpringBootTest // testes de integracao, nao de API
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // padrao é .MOCK, em que nao levanta pp web de verdade e por isso dava defeito
@TestPropertySource("/application-test.properties") // app.prop ainda usado, porem tudo que está em app-test.prop será priorizado (substituido do .prop original)
class CadastroCozinhaIT {
	/**
	 * INTEGRAÇÃO.. APAGADO PELO ANDAR DA AULA
	 */
	/*
	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;

	@Test
	public void testarCadastroCozinhaComSucesso(){
		// cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");

		// ação
		novaCozinha = cadastroCozinhaService.salvar(novaCozinha);

		// validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}

	@Test
	public void deveFalhar_QuandoCadastrarCozinhaSemNome(){
		Cozinha cozinha = new Cozinha();
		cozinha.setNome(null);
		ConstraintViolationException erroEsperado =
				Assertions.assertThrows(ConstraintViolationException.class, () -> {
					cadastroCozinhaService.salvar(cozinha);
				});

		assertThat(erroEsperado).isNotNull();
	}

	@Test
	public void deveFalhar_QuandoExcluirCozinhaEmUso(){
		Cozinha cozinha = new Cozinha();
		cozinha.setNome("Chinesa");
		Restaurante restaurante = new Restaurante();
		restaurante.setNome("res 1");
		restaurante.setTaxaFrete(new BigDecimal(5));
		restaurante.setCozinha(cozinha);

		cozinha = cadastroCozinhaService.salvar(cozinha);
		Long cozinhaId = cozinha.getId();
		restaurante = cadastroRestauranteService.salvar(restaurante);

		EntidadeEmUsoException erroEsperado =
				Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
					cadastroCozinhaService.excluir(cozinhaId);
				});

		assertThat(erroEsperado).isNotNull();
	}

	@Test
	public void deveFalhar_QuandoExcluirCozinhaInexistente(){
		Cozinha cozinha = new Cozinha();
		cozinha.setNome("Chinesa"); //CozinhaNaoEncontradaException

		cozinha = cadastroCozinhaService.salvar(cozinha);

		CozinhaNaoEncontradaException erroEsperado =
				Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> {
					cadastroCozinhaService.excluir(80l);
				});

		assertThat(erroEsperado).isNotNull();
	}
	 */

	/**
	 * TESTES DE API (usa controller, chamada http, essas coisas)
	 */
	@LocalServerPort
	private int port;

	@Autowired
	private DatabaseCleaner databaseCleaner;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	//@Autowired // instancia do flyway, com o objetivo de rodar o arquivo de migrate antes de cada teste (que limpa toda base e refaz tudo)
	//private Flyway flyway;

	private static final int COZINHA_ID_INEXISTENTE = 100;
	private Cozinha cozinhaAmericana;
	private int quantidadeCozinhasCadastradas;
	private String jsonCorretoCozinhaChinesa;

	@BeforeEach
	public void setUp(){
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // permite logar quando falhar
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
	//	flyway.migrate();
		databaseCleaner.clearTables();
		prepararDados();
	}

	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas(){
		RestAssured.given()

				.accept(ContentType.JSON)
			.when()
				.get()
			.then()
				.statusCode(200); // ou statusCode(HttpStatus.OK.value());
	}

	@Test
	public void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas(){
		RestAssured.given()
				.accept(ContentType.JSON)
			.when()
				.get()
			.then()
				.body("", Matchers.hasSize(quantidadeCozinhasCadastradas))
				.body("nome", Matchers.hasItems("Americana", "Tailandesa"));
	}
	// ordem dos testes sao "aleatorias". Um teste nao pode depender de outro.
	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha(){
		RestAssured.given()
				.body(jsonCorretoCozinhaChinesa)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente(){
		RestAssured.given()
				.pathParam("cozinhaId", 2)
				.accept(ContentType.JSON)
			.when()
				.get("{cozinhaId}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("nome", Matchers.equalTo("Americana"));
	}

	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente(){
		RestAssured.given()
				.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
				.accept(ContentType.JSON)
			.when()
				.get("{cozinhaId}")
			.then()
				.statusCode(HttpStatus.NOT_FOUND.value());

	}

	private void prepararDados(){
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource("/json/correto/cozinha-chinesa.json");
		adicionarCozinhas("Tailandesa", "Americana");
//		Cozinha c1 = new Cozinha();
//		c1.setNome("Tailandesa");
//		cozinhaRepository.save(c1);
//
//		Cozinha c2 = new Cozinha();
//		c2.setNome("Americana");
//		cozinhaRepository.save(c2);
	}

	private void adicionarCozinhas(String... nomes){
		Arrays.stream(nomes).forEach(x -> {
			Cozinha c = new Cozinha();
			c.setNome(x);
			cozinhaRepository.save(c);
			quantidadeCozinhasCadastradas++;
		});
	}
}
