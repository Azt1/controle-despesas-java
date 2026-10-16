package br.com.azt1.despesas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import br.com.azt1.despesas.model.TipoLancamento;
import br.com.azt1.despesas.repository.LancamentoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SaldoMensalTest {

    private LancamentoService service;
    private static final LocalDate DATA = LocalDate.of(2026, 10, 16);
    private static final YearMonth MES = YearMonth.of(2026, 10);

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
    }

    @ParameterizedTest
    @CsvSource({
        "100.00, 30.00, 70.00",
        "30.00, 100.00, -70.00",
        "10.00, 10.00, 0.00",
        "1.10, 0.80, 0.30"
    })
    void subtraiDespesasDasReceitas(String receita, String despesa, String esperado) {
        service.cadastrar("Receita", new BigDecimal(receita), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Despesa", new BigDecimal(despesa), DATA, TipoLancamento.DESPESA);

        assertEquals(new BigDecimal(esperado), service.calcularSaldo(MES));
    }

    @Test
    void mesSemMovimentoTemSaldoZero() {
        assertEquals(BigDecimal.ZERO, service.calcularSaldo(MES));
    }

    @ParameterizedTest
    @EnumSource(TipoLancamento.class)
    void consideraMesComApenasUmTipo(TipoLancamento tipo) {
        BigDecimal valor = new BigDecimal("10.00");
        service.cadastrar("Teste", valor, DATA, tipo);

        BigDecimal esperado = tipo == TipoLancamento.RECEITA ? valor : valor.negate();
        assertEquals(esperado, service.calcularSaldo(MES));
    }

    @Test
    void ignoraOutrosMesesEAnos() {
        service.cadastrar("Receita de outubro", new BigDecimal("100.00"), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Despesa de novembro", new BigDecimal("999.00"), DATA.plusMonths(1), TipoLancamento.DESPESA);
        service.cadastrar("Despesa do ano anterior", new BigDecimal("999.00"), DATA.minusYears(1), TipoLancamento.DESPESA);

        assertEquals(new BigDecimal("100.00"), service.calcularSaldo(MES));
    }

    @Test
    void consultaNaoAlteraOsLancamentos() {
        service.cadastrar("Salário", new BigDecimal("100.00"), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Compra", new BigDecimal("30.00"), DATA, TipoLancamento.DESPESA);

        assertEquals(service.calcularSaldo(MES), service.calcularSaldo(MES));
        assertEquals(2, service.listar().size());
        assertEquals(new BigDecimal("100.00"), service.listar().get(0).getValor());
        assertEquals(new BigDecimal("30.00"), service.listar().get(1).getValor());
    }

    @Test
    void rejeitaMesNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.calcularSaldo(null));
    }

    @Test
    void preservaPrecisaoComValoresGrandes() {
        service.cadastrar("Receita", new BigDecimal("12345678901234567890.12"), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Despesa", new BigDecimal("0.02"), DATA, TipoLancamento.DESPESA);

        assertEquals(new BigDecimal("12345678901234567890.10"), service.calcularSaldo(MES));
    }
}
