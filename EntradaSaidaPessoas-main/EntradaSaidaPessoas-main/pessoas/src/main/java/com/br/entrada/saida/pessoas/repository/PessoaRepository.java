package com.br.entrada.saida.pessoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.br.entrada.saida.pessoas.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
