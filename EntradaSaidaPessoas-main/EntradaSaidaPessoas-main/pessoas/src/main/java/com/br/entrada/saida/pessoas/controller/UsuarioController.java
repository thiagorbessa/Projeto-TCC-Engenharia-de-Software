package com.br.entrada.saida.pessoas.controller;

import com.br.entrada.saida.pessoas.model.Usuario;
import com.br.entrada.saida.pessoas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/novo")
    public String novo(Model model, Authentication auth) {
        model.addAttribute("usuario", new Usuario());

        // Verifica se quem está logado é GERAL para permitir escolher perfil
        boolean isGeral = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GERAL"));

        model.addAttribute("podeEscolherPerfil", isGeral);
        return "usuarios/formulario";
    }

    @PostMapping
    public String salvar(Usuario usuario, Authentication auth) {
        // 1. Limpeza de CPF
        if (usuario.getCpf() != null) {
            usuario.setCpf(usuario.getCpf().replaceAll("[^\\d]", ""));
        }

        // 2. Identifica quem está logado (SISTEMA ou GERAL)
        boolean isGeral = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GERAL"));

        if (usuario.getId() != null) {

            Usuario usuarioBanco = usuarioRepository.findById(usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Atualiza campos básicos
            usuarioBanco.setNomeCompleto(usuario.getNomeCompleto());
            usuarioBanco.setCpf(usuario.getCpf());

            // Atualiza Perfil (Somente se quem está logado for GERAL ou SISTEMA)
            // Isso evita que um usuário comum "se promova" via requisição manual
            if (isGeral || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SISTEMA"))) {
                usuarioBanco.setPerfil(usuario.getPerfil());
            }

            // Atualiza Senha apenas se algo foi digitado no campo
            if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
                usuarioBanco.setSenha(encoder.encode(usuario.getSenha()));
            }
            // Se a senha veio vazia, o usuarioBanco mantém a senha antiga que já estava nele

            usuarioRepository.save(usuarioBanco);

        } else {
            // ---  NOVO CADASTRO ---

            // Se não for GERAL, força o novo usuário a ser sempre ROLE_USER
            if (!isGeral) {
                usuario.setPerfil("ROLE_USER");
            }

            // Senha é obrigatória no novo cadastro
            usuario.setSenha(encoder.encode(usuario.getSenha()));
            usuarioRepository.save(usuario);
        }

        return "redirect:/usuarios";
    }

    @GetMapping
    public String listar(Model model, Authentication auth) {
        List<Usuario> usuarios;

        boolean isGeral = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GERAL"));

        if (isGeral) {
            // Admin Geral vê a lista completa
            usuarios = usuarioRepository.findAll();
        } else {
            // Admin de Sistema vê apenas quem é ROLE_USER
            usuarios = usuarioRepository.findAll().stream()
                    .filter(u -> u.getPerfil().equals("ROLE_USER"))
                    .collect(Collectors.toList());
        }

        model.addAttribute("usuarios", usuarios);
        return "usuarios/listar-usuarios";
    }
    // Método para Deletar Usuário
    @DeleteMapping("/{id}")
    public String excluir(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios";
    }

    // Método para Abrir Edição
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido:" + id));

        boolean isGeral = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GERAL"));

        model.addAttribute("usuario", usuario);
        model.addAttribute("podeEscolherPerfil", isGeral);
        return "usuarios/formulario"; // Reutiliza o formulário de cadastro
    }
}