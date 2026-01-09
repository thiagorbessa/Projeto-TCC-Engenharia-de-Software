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
		return this.id == null;
		
	}

	
	public boolean jaEntrou() {
	    return this.horaEntrada != null; 
	}
	
	
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
	    if (!jaEntrou()) { 
	        
	        LocalDateTime entrada = LocalDateTime.now();
	        
	        DateTimeFormatter formatarEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	        this.horaEntrada = entrada.format(formatarEntrada);
	    }
	    return this.horaEntrada; 
	}
	 
	 public String getHoraSaida() {
	        if (horaSaida != null) {
	            
	            return horaSaida;
	        } else {
	            
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
