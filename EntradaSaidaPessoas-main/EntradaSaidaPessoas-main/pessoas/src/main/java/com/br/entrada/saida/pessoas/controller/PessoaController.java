package com.br.entrada.saida.pessoas.controller;

import com.br.entrada.saida.pessoas.repository.PessoaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {

	private final PessoaRepository pessoaRepository;

	public PessoaController(PessoaRepository pessoaRepository) {
		this.pessoaRepository = pessoaRepository;
	}

	@GetMapping
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("listar-pessoas");
		mv.addObject("pessoas", pessoaRepository.findAll());
		return mv;
	}

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("formulario-pessoa");
		mv.addObject("pessoa", new com.br.entrada.saida.pessoas.model.Pessoa());
		return mv;
	}

	@PostMapping
	public String salvar(com.br.entrada.saida.pessoas.model.Pessoa pessoa) {
		pessoaRepository.save(pessoa);
		return "redirect:/pessoas";
	}

	@GetMapping("/{id}/editar")
	public ModelAndView editar(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("formulario-pessoa");
		mv.addObject("pessoa", pessoaRepository.findById(id).orElseThrow());
		return mv;
	}
}
