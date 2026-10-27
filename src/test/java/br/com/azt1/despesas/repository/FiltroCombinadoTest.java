package br.com.azt1.despesas.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.azt1.despesas.model.Lancamento;
import br.com.azt1.despesas.model.TipoLancamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FiltroCombinadoTest {

    private LancamentoRepository repository;
    private static final YearMonth MES = YearMonth.of(2026, 10);

    @BeforeEach
    void preparar() {
        repository = new LancamentoRepository();
        salvar("Mercado", "2026-10-26", TipoLancamento.DESPESA);
        salvar("Salário", "2026-10-26", TipoLancamento.RECEITA);
        salvar("Novembro", "2026-11-26", TipoLancamento.DESPESA);
        salvar("Outro ano", "2025-10-26", TipoLancamento.DESPESA);
    }

    private void salvar(String descricao, String data, TipoLancamento tipo) {
        repository.salvar(new Lancamento(descricao, new BigDecimal("10.00"), LocalDate.parse(data), tipo));
    }

    @Test
    void filtraMesAnoETipo() {
        var resultado = repository.listarPorMesETipo(MES, TipoLancamento.DESPESA);
        assertEquals(1, resultado.size());
        assertEquals("Mercado", resultado.get(0).getDescricao());
    }

    @Test
    void permiteConsultarReceitas() {
        assertEquals("Salário", repository.listarPorMesETipo(MES, TipoLancamento.RECEITA).get(0).getDescricao());
    }

    @Test
    void retornaVazioSemCorrespondencia() {
        assertTrue(repository.listarPorMesETipo(MES.plusMonths(1), TipoLancamento.RECEITA).isEmpty());
    }

    @Test
    void resultadoNaoPermiteApagarDadosInternos() {
        repository.listarPorMesETipo(MES, TipoLancamento.DESPESA).clear();
        assertEquals(4, repository.listar().size());
        assertEquals(1, repository.listarPorMesETipo(MES, TipoLancamento.DESPESA).size());
    }

    @Test
    void rejeitaMesNulo() {
        assertThrows(IllegalArgumentException.class, () -> repository.listarPorMesETipo(null, TipoLancamento.DESPESA));
    }

    @Test
    void rejeitaTipoNulo() {
        assertThrows(IllegalArgumentException.class, () -> repository.listarPorMesETipo(MES, null));
    }
}
