package com.br.entrada.saida.pessoas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;

    private String unidade;
    private String setor;
    private String andar;
    private String paciente;


    @ManyToOne
    @JoinColumn(name = "usuario_entrada_id")
    private Usuario usuarioEntrada; // Quem marcou a entrada

    @ManyToOne
    @JoinColumn(name = "usuario_saida_id")
    private Usuario usuarioSaida;   // Quem marcou a saída

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;
}