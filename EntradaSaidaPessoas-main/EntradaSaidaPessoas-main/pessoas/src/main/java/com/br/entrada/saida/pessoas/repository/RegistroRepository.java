package com.br.entrada.saida.pessoas.repository;

import com.br.entrada.saida.pessoas.model.Pessoa;
import com.br.entrada.saida.pessoas.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {

    List<Registro> findByPessoaIdOrderByHoraEntradaDesc(Long pessoaId);

    boolean existsByPessoaIdAndHoraSaidaIsNull(Long pessoaId);

    List<Registro> findByHoraSaidaIsNull();
}


