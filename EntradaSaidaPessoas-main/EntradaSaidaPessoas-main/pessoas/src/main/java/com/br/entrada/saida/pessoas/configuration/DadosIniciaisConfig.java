package com.br.entrada.saida.pessoas.configuration;

import com.br.entrada.saida.pessoas.model.Usuario;
import com.br.entrada.saida.pessoas.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DadosIniciaisConfig {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository, PasswordEncoder encoder) {
        return args -> {
            if (repository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNomeCompleto("Administrador Geral");
                admin.setCpf("00000000000"); // Use este CPF para o primeiro login
                admin.setSenha(encoder.encode("admin123")); // Senha inicial
                admin.setPerfil("ROLE_GERAL");
                repository.save(admin);
                System.out.println(">>> Usuário ADMIN GERAL criado com sucesso! (CPF: 00000000000 / Senha: admin123)");
            }
        };
    }
}