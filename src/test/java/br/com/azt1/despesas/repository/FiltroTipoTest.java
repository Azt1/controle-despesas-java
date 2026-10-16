package br.com.azt1.despesas.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import br.com.azt1.despesas.model.Lancamento;
import br.com.azt1.despesas.model.TipoLancamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FiltroTipoTest {

    private Lancamento lancamento(TipoLancamento tipo) {
        return new Lancamento("Teste", new BigDecimal("10.00"), LocalDate.of(2026, 10, 16), tipo);
    }

    @ParameterizedTest
    @EnumSource(TipoLancamento.class)
    void listaSomenteTipoEscolhido(TipoLancamento tipo) {
        LancamentoRepository repository = new LancamentoRepository();
        repository.salvar(lancamento(TipoLancamento.RECEITA));
        repository.salvar(lancamento(TipoLancamento.DESPESA));

        List<Lancamento> resultado = repository.listarPorTipo(tipo);

        assertEquals(1, resultado.size());
        assertEquals(tipo, resultado.get(0).getTipo());
    }

    @Test
    void retornaListaVaziaQuandoNaoHaTipoProcurado() {
        LancamentoRepository repository = new LancamentoRepository();
        repository.salvar(lancamento(TipoLancamento.RECEITA));

        assertTrue(repository.listarPorTipo(TipoLancamento.DESPESA).isEmpty());
    }

    @Test
    void alterarResultadoNaoApagaLancamentos() {
        LancamentoRepository repository = new LancamentoRepository();
        repository.salvar(lancamento(TipoLancamento.RECEITA));

        repository.listarPorTipo(TipoLancamento.RECEITA).clear();

        assertEquals(1, repository.listar().size());
    }

    @Test
    void rejeitaTipoNulo() {
        LancamentoRepository repository = new LancamentoRepository();

        assertThrows(IllegalArgumentException.class, () -> repository.listarPorTipo(null));
    }
}
