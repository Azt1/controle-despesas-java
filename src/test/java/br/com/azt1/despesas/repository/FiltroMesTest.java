package br.com.azt1.despesas.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.azt1.despesas.model.Lancamento;
import br.com.azt1.despesas.model.TipoLancamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FiltroMesTest {

    private void salvar(LancamentoRepository repository, LocalDate data) {
        repository.salvar(new Lancamento("Teste", new BigDecimal("10.00"), data, TipoLancamento.DESPESA));
    }

    @Test
    void incluiPrimeiroEUltimoDiaDoMesSemMisturarAnos() {
        LancamentoRepository repository = new LancamentoRepository();
        salvar(repository, LocalDate.of(2026, 10, 1));
        salvar(repository, LocalDate.of(2026, 10, 31));
        salvar(repository, LocalDate.of(2026, 9, 30));
        salvar(repository, LocalDate.of(2026, 11, 1));
        salvar(repository, LocalDate.of(2025, 10, 16));

        List<Lancamento> resultado = repository.listarPorMes(YearMonth.of(2026, 10));

        assertEquals(2, resultado.size());
        assertEquals(LocalDate.of(2026, 10, 1), resultado.get(0).getData());
        assertEquals(LocalDate.of(2026, 10, 31), resultado.get(1).getData());
    }

    @Test
    void retornaListaVaziaParaMesSemMovimento() {
        LancamentoRepository repository = new LancamentoRepository();
        salvar(repository, LocalDate.of(2026, 10, 16));

        assertTrue(repository.listarPorMes(YearMonth.of(2026, 11)).isEmpty());
    }

    @Test
    void separaDezembroEJaneiroDoAnoSeguinte() {
        LancamentoRepository repository = new LancamentoRepository();
        salvar(repository, LocalDate.of(2025, 12, 31));
        salvar(repository, LocalDate.of(2026, 1, 1));

        List<Lancamento> resultado = repository.listarPorMes(YearMonth.of(2026, 1));

        assertEquals(1, resultado.size());
        assertEquals(LocalDate.of(2026, 1, 1), resultado.get(0).getData());
    }

    @Test
    void rejeitaMesNulo() {
        LancamentoRepository repository = new LancamentoRepository();

        assertThrows(IllegalArgumentException.class, () -> repository.listarPorMes(null));
    }

    @Test
    void alterarResultadoNaoApagaLancamentos() {
        LancamentoRepository repository = new LancamentoRepository();
        salvar(repository, LocalDate.of(2026, 10, 16));

        repository.listarPorMes(YearMonth.of(2026, 10)).clear();

        assertEquals(1, repository.listar().size());
    }
}
