package com.br.entrada.saida.pessoas.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pessoas")
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String telefone;
	private String unidade;
	private String setor;
	private String andar;
	private String paciente;

	@OneToMany(
			mappedBy = "pessoa",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<RegistroEntradaSaida> registros = new ArrayList<>();

    /* =========================
       Getters e Setters
       ========================= */

	public Long getId() {
		return id;
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

	public List<RegistroEntradaSaida> getRegistros() {
		return registros;
	}

	public void adicionarRegistro(RegistroEntradaSaida registro) {
		registros.add(registro);
		registro.setPessoa(this);
	}

	public void removerRegistro(RegistroEntradaSaida registro) {
		registros.remove(registro);
		registro.setPessoa(null);
	}
}
