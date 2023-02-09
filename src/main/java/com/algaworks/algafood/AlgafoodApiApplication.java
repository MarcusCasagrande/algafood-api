package com.algaworks.algafood;

import com.algaworks.algafood.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class) // permite usar o rep custom
public class AlgafoodApiApplication {

	public static void main(String[] args) {

		/*
		BOAS PRATICAS DATA-HORA (5 leis)
		1) usar ISO-8601 pra formatar data e hora
		2) API deve aceitar qualquer fuso horario e converter corretamente
		3) Armazene na database em UTC
		4) Retorne em UTC para o front-end
		5) Não inclua horario caso nao seja necessario
		 */
		TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // faz a aplicacao responder atas ao front-end em UTC (baseado no que ta no data base, entao é bom que la esteja em UTC tambem (e está))

		// 23.46: comentado o codigo default pra usar codigo abaixo customizado com o listener da nossa classe Base64protocolResolver.java
		SpringApplication.run(AlgafoodApiApplication.class, args);
//		var app = new SpringApplication(AlgafoodApiApplication.class);
//		app.addListeners(new Base64ProtocolResolver()); // adiciona o listener ANTES de dar run
//		app.run(args);
	}

}
