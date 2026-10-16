package br.com.azt1.despesas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.azt1.despesas.model.Lancamento;
import br.com.azt1.despesas.model.TipoLancamento;
import br.com.azt1.despesas.repository.LancamentoRepository;

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

    public List<Lancamento> listarPorTipo(TipoLancamento tipo) {
        return repository.listarPorTipo(tipo);
    }

    public List<Lancamento> listarPorMes(YearMonth mes) {
        return repository.listarPorMes(mes);
    }
}
