package com.br.entrada.saida.pessoas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registros_entrada_saida")
public class RegistroEntradaSaida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private com.br.entrada.saida.pessoas.model.Pessoa pessoa;

    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;

    @Column(length = 500)
    private String observacao;

    /* ========================= */

    public Long getId() {
        return id;
    }

    public com.br.entrada.saida.pessoas.model.Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(com.br.entrada.saida.pessoas.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(LocalDateTime horaSaida) {
        this.horaSaida = horaSaida;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
