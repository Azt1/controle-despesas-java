package br.com.azt1.despesas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.azt1.despesas.model.Lancamento;
import br.com.azt1.despesas.model.TipoLancamento;
import br.com.azt1.despesas.repository.LancamentoRepository;
import br.com.azt1.despesas.dto.ResumoMensal;

@Service
public class LancamentoService {

    private final LancamentoRepository repository;

    public LancamentoService(LancamentoRepository repository) {
        this.repository = repository;
    }

    public Lancamento cadastrar(String descricao, BigDecimal valor, LocalDate data, TipoLancamento tipo) {
        Lancamento lancamento = new Lancamento(descricao, valor, data, tipo);
        repository.salvar(lancamento);
        return lancamento;
    }

    public List<Lancamento> listar() {
        return repository.listar();
    }

    public List<Lancamento> listar(YearMonth mes, TipoLancamento tipo) {
        if (mes != null && tipo != null) {
            return repository.listarPorMesETipo(mes, tipo);
        }
        if (mes != null) {
            return repository.listarPorMes(mes);
        }
        if (tipo != null) {
            return repository.listarPorTipo(tipo);
        }
        return repository.listar();
    }

    public List<Lancamento> listarPorTipo(TipoLancamento tipo) {
        return repository.listarPorTipo(tipo);
    }

    public List<Lancamento> listarPorMes(YearMonth mes) {
        return repository.listarPorMes(mes);
    }

    public BigDecimal calcularSaldo(YearMonth mes) {
        return consultarResumo(mes).saldo();
    }

    public ResumoMensal consultarResumo(YearMonth mes) {
        BigDecimal receitas = BigDecimal.ZERO;
        BigDecimal despesas = BigDecimal.ZERO;
        for (Lancamento lancamento : repository.listarPorMes(mes)) {
            if (lancamento.getTipo() == TipoLancamento.RECEITA) {
                receitas = receitas.add(lancamento.getValor());
            } else {
                despesas = despesas.add(lancamento.getValor());
            }
        }
        return new ResumoMensal(mes, receitas, despesas, receitas.subtract(despesas));
    }
}
