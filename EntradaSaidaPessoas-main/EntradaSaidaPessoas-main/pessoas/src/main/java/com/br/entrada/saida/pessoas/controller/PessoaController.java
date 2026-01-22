package com.br.entrada.saida.pessoas.controller;

import com.br.entrada.saida.pessoas.model.Pessoa;
import com.br.entrada.saida.pessoas.model.Registro;
import com.br.entrada.saida.pessoas.model.Usuario;
import com.br.entrada.saida.pessoas.repository.PessoaRepository;
import com.br.entrada.saida.pessoas.repository.RegistroRepository;
import com.br.entrada.saida.pessoas.repository.UsuarioRepository;
import com.br.entrada.saida.pessoas.service.RegistroService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pessoas")
@RequiredArgsConstructor
public class PessoaController {

	private final PessoaRepository pessoaRepository;
	private final RegistroService registroService;
	private final RegistroRepository registroRepository;
	private final UsuarioRepository usuarioRepository;

	@GetMapping
	public String listar(@RequestParam(required = false) String busca, Model model) {
		List<Pessoa> pessoas = (busca != null && !busca.isBlank())
				? pessoaRepository.buscarPorTermo(busca)
				: pessoaRepository.findAll();

		List<Registro> registrosAbertos = registroService.buscarRegistrosAbertos();
		Map<Long, Registro> registrosAbertosMap = registrosAbertos.stream()
				.collect(Collectors.toMap(r -> r.getPessoa().getId(), r -> r));

		model.addAttribute("pessoas", pessoas);
		model.addAttribute("registrosAbertos", registrosAbertosMap);
		model.addAttribute("busca", busca);
		return "listar";
	}

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("formulario");
		mv.addObject("pessoa", new Pessoa());
		return mv;
	}

	@PostMapping
	public String salvar(@ModelAttribute Pessoa pessoa, Model model, Authentication auth) {
		// Busca o usuário do sistema que está logado
		Usuario usuarioLogado = usuarioRepository.findByCpf(auth.getName())
				.orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));

		if (pessoa.getCpf() != null) pessoa.setCpf(pessoa.getCpf().replaceAll("[^\\d]", ""));
		if (pessoa.getTelefone() != null) pessoa.setTelefone(pessoa.getTelefone().replaceAll("[^\\d]", ""));

		// Validações de Documento
		if ((pessoa.getCpf() == null || pessoa.getCpf().isBlank()) &&
				(pessoa.getIdentidade() == null || pessoa.getIdentidade().isBlank())) {
			model.addAttribute("erro", "Informe pelo menos CPF ou Identidade.");
			model.addAttribute("pessoa", pessoa);
			return "formulario";
		}

		// Lógica de Edição vs Cadastro Novo com Auditoria
		if (pessoa.getId() != null) {
			Pessoa pessoaBanco = pessoaRepository.findById(pessoa.getId()).orElseThrow();

			// Atualiza campos cadastrais
			pessoaBanco.setNome(pessoa.getNome());
			pessoaBanco.setCpf(pessoa.getCpf());
			pessoaBanco.setIdentidade(pessoa.getIdentidade());
			pessoaBanco.setTelefone(pessoa.getTelefone());
			pessoaBanco.setUnidade(pessoa.getUnidade());
			pessoaBanco.setSetor(pessoa.getSetor());
			pessoaBanco.setAndar(pessoa.getAndar());
			pessoaBanco.setPaciente(pessoa.getPaciente());
			pessoaBanco.setObservacao(pessoa.getObservacao());

			// Define quem editou por último
			pessoaBanco.setUsuarioResponsavel(usuarioLogado);
			pessoaRepository.save(pessoaBanco);
		} else {
			// Define quem está criando o registro
			pessoa.setUsuarioResponsavel(usuarioLogado);
			pessoaRepository.save(pessoa);
		}

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
		Pessoa pessoa = pessoaRepository.findById(id).orElseThrow();
		List<Registro> registros = registroRepository.findByPessoaIdOrderByHoraEntradaDesc(id);

		model.addAttribute("pessoa", pessoa);
		model.addAttribute("registros", registros);
		return "historico";
	}

	@GetMapping("/buscar")
	public String buscar(@RequestParam(required = false) String termo, Model model) {
		List<Pessoa> pessoas = (termo == null || termo.isBlank())
				? pessoaRepository.findAll()
				: pessoaRepository.buscarPorTermo(termo);

		List<Registro> registrosAbertos = registroService.buscarRegistrosAbertos();
		Map<Long, Registro> registrosAbertosMap = registrosAbertos.stream()
				.collect(Collectors.toMap(r -> r.getPessoa().getId(), r -> r));

		model.addAttribute("pessoas", pessoas);
		model.addAttribute("registrosAbertos", registrosAbertosMap);
		return "listar :: tabela";
	}
}