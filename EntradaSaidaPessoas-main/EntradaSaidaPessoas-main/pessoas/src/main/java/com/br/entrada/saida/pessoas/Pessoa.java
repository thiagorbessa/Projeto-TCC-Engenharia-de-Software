package com.br.entrada.saida.pessoas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Pessoa {
	
	
	private String id;
	
    private String nome;
    private String telefone;
    private String unidade;
    private String setor;
    private String andar;
    private String paciente;
    private String horaEntrada;
    private String horaSaida;
    private String observacao;
    
    
    public Pessoa(String id, String nome, String telefone, String unidade, String setor, String andar,
    		String paciente, String observacao) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.unidade = unidade;
        this.setor = setor;
        this.andar = andar;
        this.paciente = paciente;
        this.observacao = observacao;
      
    }
	
	public Pessoa() {}
	
	public boolean isNovo() {
		return this.id == null;//para criar o cadastro será verificado se é um novo contato ou se é um cadastro já feito e 
		//deve ser feito a alteração
	}// se for true significa novo cadastro, se for false significa que é uma edicao
	//esse metodo está sendo utilizado no html
	/*
	 * <form th:action="${pessoa.novo} ? @{/pessoas} : @{/pessoas/{pessoa}(pessoa=${pessoa.id})}" 
				th:object="${pessoa}" th:method="${contato.novo} ? post : put">
				
				<div th:if="${!contato.novo}"><!-- se nao for verdadeiro nao exibe o identificador -->
					<label>Código: </label>
					<input th:field="*{id}" readonly="readonly" />
				</div>
	 * 
	 * 
	 */
	
	public boolean jaEntrou() {
	    return this.horaEntrada != null; // Verifica se a pessoa já tem a horaEntrada salva
	}//da mesma forma esse metodo irá verificar se a pessoa ja fez cadastro e tem a horaEntrada Salva, se ja tiver quando
	//editar nao deve alterar a horaEntrada
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	public String getSetor() {
		return setor;
	}
	public void setSetor(String setor) {
		this.setor = setor;
	}
	public String getAndar() {
		return andar;
	}
	public void setAndar(String andar) {
		this.andar = andar;
	}
	public String getPaciente() {
		return paciente;
	}
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getHoraEntrada() {
	    if (!jaEntrou()) { // Verifica se horaEntrada é nula, se for, adiciona a horaEntrada
	        // Obtém a data e hora atual
	        LocalDateTime entrada = LocalDateTime.now();
	        // Formata a data e hora atual como uma string
	        DateTimeFormatter formatarEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	        this.horaEntrada = entrada.format(formatarEntrada);
	    }
	    return this.horaEntrada; // Retorna a horaEntrada já cadastrada ou recém cadastrada
	}
	 
	 public String getHoraSaida() {
	        if (horaSaida != null) {
	            // Se a hora de saída foi registrada, retorna ela
	            return horaSaida;
	        } else {
	            // Se a hora de saída não foi registrada, retorna uma string vazia
	            return "";
	        }
	    }
	 public void sethoraEntrada(String horaEntrada) {
	        this.horaEntrada = horaEntrada;
	    }
	 
	 public void setHoraSaida(String horaSaida) {
	        this.horaSaida = horaSaida;
	    }
	 public void setHoraSaida(LocalDateTime horaSaida) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	        this.horaSaida = horaSaida.format(formatter);
	    }
	
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	

    
    

}
