package com.br.entrada.saida.pessoas.controller;

import com.br.entrada.saida.pessoas.model.Pessoa;
import com.br.entrada.saida.pessoas.model.Registro;
import com.br.entrada.saida.pessoas.repository.PessoaRepository;
import com.br.entrada.saida.pessoas.repository.RegistroRepository;
import com.br.entrada.saida.pessoas.service.RegistroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {

	private final PessoaRepository pessoaRepository;
	private final RegistroService registroService;
	private final RegistroRepository registroRepository;


	public PessoaController(PessoaRepository pessoaRepository,
                            RegistroService registroService, RegistroRepository registroRepository) {
		this.pessoaRepository = pessoaRepository;
		this.registroService = registroService;
        this.registroRepository = registroRepository;
    }


	@GetMapping
	public String listar(Model model) {

		List<Pessoa> pessoas = pessoaRepository.findAll();

		List<Registro> registrosAbertos =
				registroService.buscarRegistrosAbertos();

		// pessoaId -> registro aberto
		Map<Long, Registro> registrosAbertosMap =
				registrosAbertos.stream()
						.collect(Collectors.toMap(
								r -> r.getPessoa().getId(),
								r -> r
						));

		model.addAttribute("pessoas", pessoas);
		model.addAttribute("registrosAbertos", registrosAbertosMap);

		return "listar";
	}


	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("formulario");
		mv.addObject("pessoa", new Pessoa());
		return mv;
	}

	@PostMapping
	public String salvar(@ModelAttribute Pessoa pessoa, Model model) {

		boolean cpfVazio = pessoa.getCpf() == null || pessoa.getCpf().isBlank();
		boolean identidadeVazia = pessoa.getIdentidade() == null || pessoa.getIdentidade().isBlank();

		if (cpfVazio && identidadeVazia) {
			model.addAttribute("erro", "Informe CPF ou Identidade");
			model.addAttribute("pessoa", pessoa);
			return "formulario";
		}

		pessoaRepository.save(pessoa);
		return "redirect:/pessoas";
	}


	@GetMapping("/{id}/editar")
	public ModelAndView editar(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("formulario");
		mv.addObject("pessoa", pessoaRepository.findById(id).orElseThrow());
		return mv;
	}


	@DeleteMapping("/{id}")
	public String remover(@PathVariable Long id) {
		pessoaRepository.deleteById(id);
		return "redirect:/pessoas";
	}

	@GetMapping("/{id}/historico")
	public String historico(@PathVariable Long id, Model model) {

		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

		List<Registro> registros =
				registroRepository.findByPessoaIdOrderByHoraEntradaDesc(id);

		model.addAttribute("pessoa", pessoa);
		model.addAttribute("registros", registros);

		return "historico";
	}


}
