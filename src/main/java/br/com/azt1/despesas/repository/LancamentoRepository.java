package br.com.azt1.despesas.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.azt1.despesas.model.Lancamento;

@Repository
public class LancamentoRepository {

    private final List<Lancamento> lancamentos = new ArrayList<>();

    public synchronized void salvar(Lancamento lancamento) {
        if (lancamento == null) {
            throw new IllegalArgumentException("O lançamento é obrigatório.");
        }
        lancamentos.add(lancamento);
    }

    public synchronized List<Lancamento> listar() {
        return new ArrayList<>(lancamentos);
    }
}
