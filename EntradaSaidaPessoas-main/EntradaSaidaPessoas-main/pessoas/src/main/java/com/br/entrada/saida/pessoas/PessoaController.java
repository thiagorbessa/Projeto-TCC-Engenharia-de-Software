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

@Controller //define a classe como classe controller
public class PessoaController {
	
	private static final ArrayList<Pessoa> LISTA_PESSOA = new ArrayList<>();//criar uma lista que vai armazenar 
	//todas as listas de pessoa

	//private static final Pessoa[] LISTA_PESSOA2 = new Pessoa[10];//esse era o antigo mas pela necessidade de 
	//ter que escolher a quantidade maximo o ArrayList é melhor
	
	static {//bloco estatico
		//Pessoa pessoa = new Pessoa();
		//pessoa.setId("1"); adiciona todas as funcoes
		//LISTA_PESSOA.add(pessoa)
		

		//Pessoa pessoa = new Pessoa("1","Thiago","4045","hc4","informatica",
		//"s1","funcionario","");//outra forma
		
		/*LISTA_PESSOA.add(new Pessoa("1","Thiago","4045","hc4","informatica",
				"s1","funcionario",""));//forma mais direta
		LISTA_PESSOA.add(new Pessoa("2","Diego","4045","hc4","informatica",
				"s1","funcionario",""));//forma mais direta
		LISTA_PESSOA.add(new Pessoa("3","Matheus","4045","hc4","informatica",
				"s1","funcionario",""));//forma mais direta
		LISTA_PESSOA.add(new Pessoa("4","Guttemberg","4045","hc4","informatica",
				"s1","funcionario",""));//forma mais direta
		LISTA_PESSOA.add(new Pessoa("5","Marcio","4045","hc4","informatica",
				"s1","funcionario",""));//forma mais direta*/
		
		
	}
	
	@GetMapping("/")//voce esta indicando se voce digitar somente localhost8080 
	public String index() {//ele vai procurar em resourses> template  o return sinalizado
		return "index";//coloca o nome do arquivo
	}
	
	@GetMapping("/pessoas")//voce esta indicando se voce digitar somente localhost:8080/contatos 
	public ModelAndView listar() {//ele vai procurar em resourses> template  o return sinalizado
		ModelAndView modelAndView = new ModelAndView("listar");//adiciona o html de retorno
		//retorna um modelo de visualizacao
		//adiciona um objeto
		modelAndView.addObject("pessoas",LISTA_PESSOA);// pessoas é a referencia que faz a lista que voce esta passando
		return modelAndView;//coloca o nome do arquivo
	}
	
	//@GetMapping("/pessoas/novo")//voce esta indicando se voce digitar somente localhost:8080/pessoas/novo
	// public String novo() {//ele vai procurar em resourses> template  o return sinalizado
	//	return "formulario";//coloca o nome do arquivo
	//esse metodo nao pode ser pois precisa retornar algo para <form th:action="${pessoa.novo} ? @{/pessoas} : @{/pessoas/{pessoa}(pessoa=${pessoa.id})}" 
	//th:object="${pessoa}" th:method="${contato.novo} ? post : put">
	@GetMapping("/pessoas/novo")//aqui voce esta indicando se voce digitar somente localhost:8080/pessoas/novo
	public ModelAndView novo() {//então, como o metodo acima de lsitar, deverá ser retornado algo
		ModelAndView modelAndView = new ModelAndView("formulario");//aqui o nome do arquivo html
		// Cria uma nova instância de Pessoa e a adiciona ao ModelAndView
	    // Isso fornecerá um objeto Pessoa vazio para ser preenchido no formulário
	    modelAndView.addObject("pessoa", new Pessoa());//aqui voce adiciona uma instancia de Pessoa
		return modelAndView;
	}
	
	//em contatos/novo quando clicar em salvar vai acionar esse metodo
	@PostMapping("/pessoas")
	public String cadastrar(Pessoa pessoa) {//quando acionar essa instancia vai ser construido uma nova pessoa
		String id = UUID.randomUUID().toString();//metodo string que gera um Id
		pessoa.setId(id);//coloca esse valor no id de pessoa
		// ao colocar esse id o metodo verificador de novo pessoa vai retornar falso e vai adicionar novo pessoa
		
		LISTA_PESSOA.add(pessoa);
		
		return "redirect:/pessoas";//vai voltar para a lista de pessoas
	}
	
	@GetMapping("/formularioSaida")//voce esta indicando se voce digitar somente localhost:8080/contatos 
	public String formularioSAida() {//ele vai procurar em resourses> template  o return sinalizado
		return "formularioSaida";//coloca o nome do arquivo
	}
	
	@GetMapping("pessoas/{id}/editar")//{id} é um valor de variavel que será enviado conforme o id gerado
	public ModelAndView editar(@PathVariable String id) {//@PathVariable vai pegar o valor colocado entre {}no path
		//informado acima e colocar no String id /String id tem que ser igual ao {id}
		ModelAndView modelAndView = new ModelAndView("formulario");
		
		Pessoa pessoa = procurarPessoa(id);
		
		modelAndView.addObject("pessoa", pessoa);//aqui voce adiciona uma instancia de Pessoa
		
		//ao encontrar pessoa ele vai adicionar todas as informacoes e ja voltara prenchido
		return modelAndView;
	}
	
	@PutMapping("/pessoas/{id}") // Mapeia o método para requisições PUT na URL /pessoas/{id}
	public String atualizar(Pessoa pessoa) {
	    	 
		// Procura a pessoa na lista com base no ID da pessoa fornecid
	    Integer indice = procurarIndicePessoa(pessoa.getId());
	    
	    Pessoa pessoaComCadastroAntigo =LISTA_PESSOA.get(indice);
	    		
	    //coloca no objeto pessoa os campos imutaveis na edicao do objeto antigo pessoaComCadastroAntigo
	    pessoa.setHoraSaida(pessoaComCadastroAntigo.getHoraSaida());//garante que a horaEntrada será mantida caso ja tenha sido cadastrada
	    pessoa.sethoraEntrada(pessoaComCadastroAntigo.getHoraEntrada());//garante que a horaEntrada será mantida
	    
	        // Remove a pessoa desatualizada da lista
	        LISTA_PESSOA.remove(pessoaComCadastroAntigo);
	        
	        
	        // Adiciona a pessoa atualizada à lista
	        //LISTA_PESSOA.add(pessoa);
	        
	        //essa opcao mantem a pessoa no mesmo lugar do indice
	        LISTA_PESSOA.add(indice, pessoa);
	    
	    // Redireciona para a lista de pessoas após a atualização
	    return "redirect:/pessoas";
	}
	
	@DeleteMapping("/pessoas/{id}")
	public String remover(@PathVariable String id) {
		Pessoa pessoa = procurarPessoa(id);
		LISTA_PESSOA.remove(pessoa);		
		return "redirect:/pessoas";
	}

	
	
	
	////////////metodos auxiliares /////////
	
	private Pessoa procurarPessoa(String id) {//pega o id e procura a pessoa com esse id
		for(int i =0;i<LISTA_PESSOA.size();i++) {
			Pessoa pessoa = LISTA_PESSOA.get(i);
			if (pessoa.getId().equals(id)) {//verifica se o objeto pessoa do id acima é igual ao id informado
				return pessoa;
			}
		}
		return null;//se nao encontrar o id retorna nulo
	}
	@GetMapping("/pessoas/{id}/formularioSaida") // Mapeia o método para requisições GET na URL /pessoas/{id}/formularioSaida
	public ModelAndView cadastrarSaida(@PathVariable String id) { // Define o método que aceita um ID como parte da URL
	    ModelAndView modelAndView = new ModelAndView("formularioSaida"); // Cria um objeto ModelAndView com a view "formularioSaida"
	    
	    Pessoa pessoa = procurarPessoa(id); // Procura a pessoa com o ID fornecido
	    
	    if (pessoa == null) { // Se a pessoa não for encontrada
	        // Se a pessoa não for encontrada, redireciona para a lista de pessoas
	        modelAndView.setViewName("redirect:/pessoas");
	    } else {
	        // Se a pessoa for encontrada, adiciona o objeto "pessoa" ao modelAndView
	        modelAndView.addObject("pessoa", pessoa);
	    }
	    
	    return modelAndView; // Retorna o ModelAndView configurado
	}
	@PostMapping("/pessoas/{id}/salvarSaida") // Mapeia o método para requisições POST na URL /pessoas/{id}/salvarSaida
	public String salvarSaida(@PathVariable String id, @RequestParam("horaSaida") String horaSaida) { // Define o método que aceita um ID como parte da URL e um parâmetro horaSaida
	    Pessoa pessoa = procurarPessoa(id); // Procura a pessoa com o ID fornecido
	    if (pessoa != null) { // Se a pessoa for encontrada
	        try {
	            LocalDateTime dateTime = LocalDateTime.parse(horaSaida); // Tenta converter o parâmetro horaSaida para LocalDateTime
	            pessoa.setHoraSaida(dateTime); // Define a hora de saída na pessoa
	        } catch (DateTimeParseException e) { // Trata o erro caso a conversão falhe
	            // Tratar erro de parsing, talvez definindo uma mensagem de erro ou redirecionando
	            e.printStackTrace(); // Imprime a stack trace para debug
	        }
	    }
	    return "redirect:/pessoas"; // Redireciona para a lista de pessoas após salvar a hora de saída
	}
	
	private Integer procurarIndicePessoa(String id) {//pega o id e procura a pessoa com esse id
		for(int i =0;i<LISTA_PESSOA.size();i++) {
			Pessoa pessoa = LISTA_PESSOA.get(i);
			if (pessoa.getId().equals(id)) {//verifica se o objeto pessoa do id acima é igual ao id informado
				return i;
			}
		}
		return null;//se nao encontrar o id retorna nulo
	}
	
	
	
}
/*
 * Quando clica no http em algum botao ele procura nas classes controller ( atende requeisicoes) 
 * 
 * ele procura os metodos que tem o mesm path da requisicao (/caminho)
 * e tem o mesmo verbo http (Get, Post, Put, Delete)
 * 
 * @VerbodarequisicaoMapping(/pathderquisicao)
 * public tipoDoRetorno nomeDoMetodo(){...algoritmo
 * }
 * ModelAndView modelAndView = new ModelAndView("nome do arquivo html sem o .html")
 * modelAndView.addObject("nomeDoObjetoQueSeraUsadoNoHtml",variavelQueSeráusado);
 * 
 * se for uma variavel ja criada voce chama ela, se for uma classe esse metodo 
 * modelAndView.addObject("nomeDoObjetoQueSeraUsadoNoHtml",variavelQueSeráusado);
 * 
 * é o mesmo que se voce tivesse criando um objeto para ser atribuido no html
 * 
 * Clase classe = new Classe();
 *  modelAndView.addObject("classe",new Classe());
 * 
 * 
 */
