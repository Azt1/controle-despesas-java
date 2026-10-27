package br.com.azt1.despesas.repository;

import java.util.ArrayList;
import java.util.List;
import java.time.YearMonth;

import org.springframework.stereotype.Repository;

import br.com.azt1.despesas.model.Lancamento;
import br.com.azt1.despesas.model.TipoLancamento;

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

    public synchronized List<Lancamento> listarPorTipo(TipoLancamento tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo é obrigatório.");
        }
        List<Lancamento> resultado = new ArrayList<>();
        for (Lancamento lancamento : lancamentos) {
            if (lancamento.getTipo() == tipo) {
                resultado.add(lancamento);
            }
        }
        return resultado;
    }

    public synchronized List<Lancamento> listarPorMesETipo(YearMonth mes, TipoLancamento tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo é obrigatório.");
        }
        List<Lancamento> resultado = new ArrayList<>();
        for (Lancamento lancamento : listarPorMes(mes)) {
            if (lancamento.getTipo() == tipo) {
                resultado.add(lancamento);
            }
        }
        return resultado;
    }

    public synchronized List<Lancamento> listarPorMes(YearMonth mes) {
        if (mes == null) {
            throw new IllegalArgumentException("O mês é obrigatório.");
        }
        List<Lancamento> resultado = new ArrayList<>();
        for (Lancamento lancamento : lancamentos) {
            if (YearMonth.from(lancamento.getData()).equals(mes)) {
                resultado.add(lancamento);
            }
        }
        return resultado;
    }
}
