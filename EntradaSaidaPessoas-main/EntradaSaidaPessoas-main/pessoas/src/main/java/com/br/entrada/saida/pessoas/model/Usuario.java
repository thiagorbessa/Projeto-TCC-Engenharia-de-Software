package com.br.entrada.saida.pessoas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;

    @Column(unique = true)
    private String cpf;

    private String senha;

    private String perfil; // "ROLE_USER", "ROLE_SISTEMA", "ROLE_GERAL"


    public boolean isNovo() {
        return id == null;
    }
}