package com.br.entrada.saida.pessoas.service;

import com.br.entrada.saida.pessoas.model.Pessoa;
import com.br.entrada.saida.pessoas.model.Registro;
import com.br.entrada.saida.pessoas.model.Usuario;
import com.br.entrada.saida.pessoas.repository.PessoaRepository;
import com.br.entrada.saida.pessoas.repository.RegistroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final PessoaRepository pessoaRepository;


    @Transactional
    public void registrarEntrada(UUID pessoaId, Usuario operador) {

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));

        boolean existeAberto = registroRepository
                .existsByPessoaIdAndHoraSaidaIsNull(pessoaId);

        if (existeAberto) {
            throw new IllegalStateException("Pessoa já possui uma entrada aberta no sistema.");
        }

        Registro registro = new Registro();
        registro.setPessoa(pessoa);
        registro.setHoraEntrada(LocalDateTime.now());

        // AUDITORIA: Quem está realizando a entrada
        registro.setUsuarioEntrada(operador);

        // Salva o estado atual da pessoa no momento da entrada
        // Isso é importante caso os dados da pessoa mudem futuramente.
        registro.setUnidade(pessoa.getUnidade());
        registro.setSetor(pessoa.getSetor());
        registro.setAndar(pessoa.getAndar());
        registro.setPaciente(pessoa.getPaciente());

        registroRepository.save(registro);
    }


    @Transactional
    public void registrarSaida(UUID registroId, Usuario operador) {

        Registro registro = registroRepository.findById(registroId)
                .orElseThrow(() -> new IllegalArgumentException("Registro de entrada não encontrado"));

        //  Não pode finalizar um registro que já possui hora de saída
        if (registro.getHoraSaida() != null) {
            throw new IllegalStateException("Este registro já foi finalizado anteriormente.");
        }

        registro.setHoraSaida(LocalDateTime.now());

        //  Quem está realizando a saída (pode ser um operador diferente da entrada)
        registro.setUsuarioSaida(operador);

        registroRepository.save(registro);
    }

    public List<Registro> buscarRegistrosAbertos() {
        return registroRepository.findByHoraSaidaIsNull();
    }
}