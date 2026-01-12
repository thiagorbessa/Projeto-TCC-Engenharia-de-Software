package com.br.entrada.saida.pessoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.br.entrada.saida.pessoas.model.Pessoa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    @Query("""
        SELECT p FROM Pessoa p
        WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%'))
           OR LOWER(p.cpf) LIKE LOWER(CONCAT('%', :termo, '%'))
           OR LOWER(p.identidade) LIKE LOWER(CONCAT('%', :termo, '%'))
           OR LOWER(p.telefone) LIKE LOWER(CONCAT('%', :termo, '%'))
    """)
    List<Pessoa> buscarPorTermo(@Param("termo") String termo);
}
