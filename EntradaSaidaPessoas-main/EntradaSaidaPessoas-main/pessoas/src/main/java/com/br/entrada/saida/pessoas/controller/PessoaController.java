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
import java.util.Optional; // <--- Não esqueça deste import
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
	public String listar(@RequestParam(required = false) String busca, Model model) {
		List<Pessoa> pessoas;

		if (busca != null && !busca.isBlank()) {
			pessoas = pessoaRepository.buscarPorTermo(busca);
			model.addAttribute("busca", busca);
		} else {
			pessoas = pessoaRepository.findAll();
		}

		List<Registro> registrosAbertos = registroService.buscarRegistrosAbertos();

		Map<Long, Registro> registrosAbertosMap = registrosAbertos.stream()
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

		if (pessoa.getCpf() != null) {
			pessoa.setCpf(pessoa.getCpf().replaceAll("[^\\d]", ""));
		}
		if (pessoa.getTelefone() != null) {
			pessoa.setTelefone(pessoa.getTelefone().replaceAll("[^\\d]", ""));
		}

		// 1. Verifica se ambos estão vazios (Regra de negócio: pelo menos um documento é obrigatório)
		boolean cpfPreenchido = pessoa.getCpf() != null && !pessoa.getCpf().isBlank();
		boolean rgPreenchido = pessoa.getIdentidade() != null && !pessoa.getIdentidade().isBlank();

		if (!cpfPreenchido && !rgPreenchido) {
			model.addAttribute("erro", "Informe pelo menos CPF ou Identidade.");
			model.addAttribute("pessoa", pessoa);
			return "formulario";
		}

		// 2. Validação de CPF (Somente se o usuário digitou algo)
		if (cpfPreenchido) {
			Optional<Pessoa> existente = pessoaRepository.findByCpf(pessoa.getCpf());
			if (existente.isPresent() && (pessoa.getId() == null || !existente.get().getId().equals(pessoa.getId()))) {
				model.addAttribute("erro", "Este CPF já pertence a: " + existente.get().getNome());
				model.addAttribute("pessoa", pessoa);
				return "formulario";
			}
		}

		// 3. Validação de Identidade/RG (Somente se o usuário digitou algo)
		if (rgPreenchido) {
			Optional<Pessoa> existente = pessoaRepository.findByIdentidade(pessoa.getIdentidade());
			if (existente.isPresent() && (pessoa.getId() == null || !existente.get().getId().equals(pessoa.getId()))) {
				model.addAttribute("erro", "Esta Identidade já pertence a: " + existente.get().getNome());
				model.addAttribute("pessoa", pessoa);
				return "formulario";
			}
		}

		// 4. Validação de Nome (Sempre obrigatório)
		if (pessoa.getNome() != null && !pessoa.getNome().isBlank()) {
			Optional<Pessoa> existente = pessoaRepository.findByNomeIgnoreCase(pessoa.getNome());
			if (existente.isPresent() && (pessoa.getId() == null || !existente.get().getId().equals(pessoa.getId()))) {
				model.addAttribute("erro", "Já existe uma pessoa cadastrada com o nome: " + pessoa.getNome());
				model.addAttribute("pessoa", pessoa);
				return "formulario";
			}
		}

		if (pessoa.getId() != null) {
			// É uma edição! Buscamos a pessoa original do banco de dados
			Pessoa pessoaBanco = pessoaRepository.findById(pessoa.getId())
					.orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

			// Atualizamos apenas os dados cadastrais da "pessoaBanco"
			pessoaBanco.setNome(pessoa.getNome());
			pessoaBanco.setCpf(pessoa.getCpf());
			pessoaBanco.setIdentidade(pessoa.getIdentidade());
			pessoaBanco.setTelefone(pessoa.getTelefone());
			pessoaBanco.setUnidade(pessoa.getUnidade());
			pessoaBanco.setSetor(pessoa.getSetor());
			pessoaBanco.setAndar(pessoa.getAndar());
			pessoaBanco.setPaciente(pessoa.getPaciente());
			pessoaBanco.setObservacao(pessoa.getObservacao());

			// Agora salvamos a 'pessoaBanco', que ainda possui a lista de registros vinculada
			pessoaRepository.save(pessoaBanco);
		} else {
			// É um cadastro novo, não tem histórico ainda
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
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));

		List<Registro> registros = registroRepository.findByPessoaIdOrderByHoraEntradaDesc(id);

		model.addAttribute("pessoa", pessoa);
		model.addAttribute("registros", registros);

		return "historico";
	}

	@GetMapping("/buscar")
	public String buscar(@RequestParam(required = false) String termo, Model model) {
		List<Pessoa> pessoas;
		if (termo == null || termo.isBlank()) {
			pessoas = pessoaRepository.findAll();
		} else {
			pessoas = pessoaRepository.buscarPorTermo(termo);
		}
		model.addAttribute("pessoas", pessoas);
		model.addAttribute("busca", termo);

		List<Registro> registrosAbertos = registroService.buscarRegistrosAbertos();
		Map<Long, Registro> registrosAbertosMap = registrosAbertos.stream()
				.collect(Collectors.toMap(r -> r.getPessoa().getId(), r -> r));
		model.addAttribute("registrosAbertos", registrosAbertosMap);

		return "listar :: tabela";
	}
}