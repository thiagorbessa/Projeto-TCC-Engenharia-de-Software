package com.br.entrada.saida.pessoas.repository;

import com.br.entrada.saida.pessoas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // O Spring Security usará este método para encontrar o usuário pelo login (CPF)
    Optional<Usuario> findByCpf(String cpf);
}
