package com.br.entrada.saida.pessoas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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
	private String observacao;


	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("horaEntrada DESC")
	private List<Registro> registros = new ArrayList<>();

	@Column(length = 11)
	@Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
	private String cpf;

	@Column(length = 20)
	private String identidade;

	// Indica qual usuário do sistema cadastrou ou editou esta pessoa
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuarioResponsavel;

	@Transient
	public boolean isNovo() {
		return id == null;
	}
}
