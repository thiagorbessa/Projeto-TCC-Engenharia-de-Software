package com.br.entrada.saida.pessoas;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller 
public class PessoaController {
	
	private static final ArrayList<Pessoa> LISTA_PESSOA = new ArrayList<>();
	
	static {
	}
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/pessoas")
	public ModelAndView listar() {
		ModelAndView modelAndView = new ModelAndView("listar");
		
		
		modelAndView.addObject("pessoas",LISTA_PESSOA);
		return modelAndView;
	}
	
	
	
	
	
	
	@GetMapping("/pessoas/novo")
	public ModelAndView novo() {
		ModelAndView modelAndView = new ModelAndView("formulario");
		
	    
	    modelAndView.addObject("pessoa", new Pessoa());
		return modelAndView;
	}
	
	
	@PostMapping("/pessoas")
	public String cadastrar(Pessoa pessoa) {
		String id = UUID.randomUUID().toString();
		pessoa.setId(id);
		
		
		LISTA_PESSOA.add(pessoa);
		
		return "redirect:/pessoas";
	}
	
	@GetMapping("/formularioSaida")
	public String formularioSAida() {
		return "formularioSaida";
	}
	
	@GetMapping("pessoas/{id}/editar")
	public ModelAndView editar(@PathVariable String id) {
		
		ModelAndView modelAndView = new ModelAndView("formulario");
		
		Pessoa pessoa = procurarPessoa(id);
		
		modelAndView.addObject("pessoa", pessoa);
		
		
		return modelAndView;
	}
	
	@PutMapping("/pessoas/{id}") 
	public String atualizar(Pessoa pessoa) {
	    	 
		
	    Integer indice = procurarIndicePessoa(pessoa.getId());
	    
	    Pessoa pessoaComCadastroAntigo =LISTA_PESSOA.get(indice);
	    		
	    
	    pessoa.setHoraSaida(pessoaComCadastroAntigo.getHoraSaida());
	    pessoa.sethoraEntrada(pessoaComCadastroAntigo.getHoraEntrada());
	    
	        
	        LISTA_PESSOA.remove(pessoaComCadastroAntigo);
	        
	        
	        
	        
	        
	        
	        LISTA_PESSOA.add(indice, pessoa);
	    
	    
	    return "redirect:/pessoas";
	}
	
	@DeleteMapping("/pessoas/{id}")
	public String remover(@PathVariable String id) {
		Pessoa pessoa = procurarPessoa(id);
		LISTA_PESSOA.remove(pessoa);		
		return "redirect:/pessoas";
	}

	
	
	
	
	
	private Pessoa procurarPessoa(String id) {
		for(int i =0;i<LISTA_PESSOA.size();i++) {
			Pessoa pessoa = LISTA_PESSOA.get(i);
			if (pessoa.getId().equals(id)) {
				return pessoa;
			}
		}
		return null;
	}
	@GetMapping("/pessoas/{id}/formularioSaida") 
	public ModelAndView cadastrarSaida(@PathVariable String id) { 
	    ModelAndView modelAndView = new ModelAndView("formularioSaida"); 
	    
	    Pessoa pessoa = procurarPessoa(id); 
	    
	    if (pessoa == null) { 
	        
	        modelAndView.setViewName("redirect:/pessoas");
	    } else {
	        
	        modelAndView.addObject("pessoa", pessoa);
	    }
	    
	    return modelAndView; 
	}
	@PostMapping("/pessoas/{id}/salvarSaida") 
	public String salvarSaida(@PathVariable String id, @RequestParam("horaSaida") String horaSaida) { 
	    Pessoa pessoa = procurarPessoa(id); 
	    if (pessoa != null) { 
	        try {
	            LocalDateTime dateTime = LocalDateTime.parse(horaSaida); 
	            pessoa.setHoraSaida(dateTime); 
	        } catch (DateTimeParseException e) { 
	            
	            e.printStackTrace(); 
	        }
	    }
	    return "redirect:/pessoas"; 
	}
	
	private Integer procurarIndicePessoa(String id) {
		for(int i =0;i<LISTA_PESSOA.size();i++) {
			Pessoa pessoa = LISTA_PESSOA.get(i);
			if (pessoa.getId().equals(id)) {
				return i;
			}
		}
		return null;
	}
	
	
	
}

