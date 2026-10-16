package br.com.azt1.despesas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import br.com.azt1.despesas.model.Lancamento;
import br.com.azt1.despesas.model.TipoLancamento;
import br.com.azt1.despesas.repository.LancamentoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LancamentoServiceTest {

    private LancamentoRepository repository;
    private LancamentoService service;
    private static final LocalDate DATA = LocalDate.of(2026, 10, 16);

    @BeforeEach
    void preparar() {
        repository = new LancamentoRepository();
        service = new LancamentoService(repository);
    }

    @ParameterizedTest
    @EnumSource(TipoLancamento.class)
    void cadastraERetornaLancamento(TipoLancamento tipo) {
        Lancamento resultado = service.cadastrar("  Teste  ", new BigDecimal("10.00"), DATA, tipo);

        assertEquals("Teste", resultado.getDescricao());
        assertEquals(tipo, resultado.getTipo());
        assertEquals(1, repository.listar().size());
        assertSame(resultado, repository.listar().get(0));
    }

    @Test
    void cadastroInvalidoNaoAlteraDadosExistentes() {
        service.cadastrar("Salário", new BigDecimal("1000.00"), DATA, TipoLancamento.RECEITA);

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Compra", BigDecimal.ZERO, DATA, TipoLancamento.DESPESA));

        assertEquals(1, service.listar().size());
        assertEquals("Salário", service.listar().get(0).getDescricao());
    }

    @Test
    void descricaoVaziaNaoEhSalva() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar(" ", new BigDecimal("10.00"), DATA, TipoLancamento.DESPESA));

        assertTrue(service.listar().isEmpty());
    }

    @Test
    void listaSomenteTipoEscolhido() {
        service.cadastrar("Salário", new BigDecimal("1000.00"), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Mercado", new BigDecimal("100.00"), DATA, TipoLancamento.DESPESA);

        assertEquals(1, service.listarPorTipo(TipoLancamento.DESPESA).size());
        assertEquals("Mercado", service.listarPorTipo(TipoLancamento.DESPESA).get(0).getDescricao());
    }

    @Test
    void listaSomenteMesEscolhido() {
        service.cadastrar("Outubro", new BigDecimal("10.00"), DATA, TipoLancamento.DESPESA);
        service.cadastrar("Novembro", new BigDecimal("20.00"), DATA.plusMonths(1), TipoLancamento.DESPESA);

        assertEquals(1, service.listarPorMes(YearMonth.of(2026, 10)).size());
        assertEquals("Outubro", service.listarPorMes(YearMonth.of(2026, 10)).get(0).getDescricao());
    }

    @Test
    void listaRetornadaNaoPermiteApagarDadosInternos() {
        service.cadastrar("Mercado", new BigDecimal("10.00"), DATA, TipoLancamento.DESPESA);

        service.listar().clear();

        assertEquals(1, service.listar().size());
    }
}
