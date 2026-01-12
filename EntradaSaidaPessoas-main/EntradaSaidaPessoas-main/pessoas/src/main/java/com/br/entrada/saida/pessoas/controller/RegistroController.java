package com.br.entrada.saida.pessoas.controller;

import com.br.entrada.saida.pessoas.model.Pessoa;
import com.br.entrada.saida.pessoas.model.Registro;
import com.br.entrada.saida.pessoas.repository.PessoaRepository;
import com.br.entrada.saida.pessoas.repository.RegistroRepository;
import com.br.entrada.saida.pessoas.service.RegistroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/registros")
@RequiredArgsConstructor
public class RegistroController {

    private final RegistroService registroService;

    @PostMapping("/entrada/{pessoaId}")
    public String registrarEntrada(@PathVariable Long pessoaId) {
        registroService.registrarEntrada(pessoaId);
        return "redirect:/pessoas";
    }


    @PostMapping("/saida/{registroId}")
    public String registrarSaida(@PathVariable Long registroId) {
        registroService.registrarSaida(registroId);
        return "redirect:/pessoas";
    }

}

