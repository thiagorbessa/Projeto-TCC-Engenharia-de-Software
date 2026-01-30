package com.br.entrada.saida.pessoas.repository;

import com.br.entrada.saida.pessoas.model.Pessoa;
import com.br.entrada.saida.pessoas.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface RegistroRepository extends JpaRepository<Registro, UUID> {

    List<Registro> findByPessoaIdOrderByHoraEntradaDesc(UUID pessoaId);

    boolean existsByPessoaIdAndHoraSaidaIsNull(UUID pessoaId);

    List<Registro> findByHoraSaidaIsNull();
}


