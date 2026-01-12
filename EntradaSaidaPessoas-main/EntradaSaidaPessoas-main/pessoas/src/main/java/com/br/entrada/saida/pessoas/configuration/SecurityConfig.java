package com.br.entrada.saida.pessoas.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**").permitAll() // Home e CSS liberados
                        .requestMatchers("/usuarios/**").hasAnyRole("SISTEMA", "GERAL") // Gestão de usuários restrita
                        .requestMatchers(HttpMethod.DELETE, "/pessoas/**").hasAnyRole("SISTEMA", "GERAL") // Remover restrito
                        .requestMatchers("/pessoas/*/editar").hasAnyRole("SISTEMA", "GERAL") // Editar restrito
                        .anyRequest().authenticated() // Qualquer outra coisa exige login
                )
                .formLogin(form -> form
                        .loginPage("/") // Login será na página inicial
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/pessoas", true) // Após logar, vai para a lista
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Para salvar senhas criptografadas
    }
}