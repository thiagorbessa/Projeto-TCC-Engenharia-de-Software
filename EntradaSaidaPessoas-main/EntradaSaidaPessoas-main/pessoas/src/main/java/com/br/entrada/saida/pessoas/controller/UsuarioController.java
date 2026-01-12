package com.br.entrada.saida.pessoas.controller;

import com.br.entrada.saida.pessoas.model.Usuario;
import com.br.entrada.saida.pessoas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Import necessário
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Import necessário
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        // 1. Limpa o CPF
        if (usuario.getCpf() != null) {
            usuario.setCpf(usuario.getCpf().replaceAll("[^\\d]", ""));
        }

        // 2. Verifica as permissões de quem está logado
        boolean isGeral = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GERAL"));

        // 3. Regra de Negócio para Perfil:
        if (isGeral) {
            // Se for GERAL, ele pode criar qualquer perfil (o que vier do formulário)
            // Não fazemos nada, deixamos o valor que veio do th:field="*{perfil}"
        } else {
            // Se for SISTEMA ou outro, força para USER
            usuario.setPerfil("ROLE_USER");
        }

        // 4. Criptografia e persistência
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);

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
    @PostMapping("/{id}") // O HTML usa th:method="delete", o Spring converte para POST com um parâmetro oculto
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