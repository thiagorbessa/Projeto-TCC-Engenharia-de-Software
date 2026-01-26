package com.br.entrada.saida.pessoas;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class PessoasApplication {

	public static void main(String[] args) {
		SpringApplication.run(PessoasApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Força o Java a usar o fuso horário de Brasília, não importa onde esteja rodando
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
	}

}
