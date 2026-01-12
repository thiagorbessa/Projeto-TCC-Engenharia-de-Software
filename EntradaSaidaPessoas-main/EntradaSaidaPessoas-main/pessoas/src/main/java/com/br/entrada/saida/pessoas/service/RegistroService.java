package com.br.entrada.saida.pessoas.service;

import com.br.entrada.saida.pessoas.model.Pessoa;
import com.br.entrada.saida.pessoas.model.Registro;
import com.br.entrada.saida.pessoas.repository.PessoaRepository;
import com.br.entrada.saida.pessoas.repository.RegistroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final PessoaRepository pessoaRepository;

    // REGISTRAR ENTRADA
    public void registrarEntrada(Long pessoaId) {

        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));

        boolean existeAberto = registroRepository
                .existsByPessoaIdAndHoraSaidaIsNull(pessoaId);

        if (existeAberto) {
            throw new IllegalStateException("Pessoa já possui entrada aberta");
        }

        Registro registro = new Registro();
        registro.setPessoa(pessoa);
        registro.setHoraEntrada(LocalDateTime.now());

        // snapshot
        registro.setUnidade(pessoa.getUnidade());
        registro.setSetor(pessoa.getSetor());
        registro.setAndar(pessoa.getAndar());
        registro.setPaciente(pessoa.getPaciente());

        registroRepository.save(registro);
    }


    // REGISTRAR SAÍDA
    public void registrarSaida(Long registroId) {

        Registro registro = registroRepository.findById(registroId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Registro não encontrado"));

        // regra: não pode sair duas vezes
        if (registro.getHoraSaida() != null) {
            throw new IllegalStateException(
                    "Registro já finalizado"
            );
        }

        registro.setHoraSaida(LocalDateTime.now());
        registroRepository.save(registro);
    }

    // usado na listagem
    public List<Registro> buscarRegistrosAbertos() {
        return registroRepository.findByHoraSaidaIsNull();
    }
}
