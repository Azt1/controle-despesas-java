package br.com.azt1.despesas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import br.com.azt1.despesas.model.TipoLancamento;
import br.com.azt1.despesas.repository.LancamentoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResumoMensalTest {

    private LancamentoService service;
    private static final YearMonth MES = YearMonth.of(2026, 10);
    private static final LocalDate DATA = LocalDate.of(2026, 10, 28);

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
    }

    @ParameterizedTest
    @CsvSource({"100.00,30.00,70.00", "30.00,100.00,-70.00", "10.00,10.00,0.00", "1.10,0.80,0.30"})
    void separaTotaisECalculaSaldo(String receita, String despesa, String saldo) {
        service.cadastrar("Receita", new BigDecimal(receita), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Despesa", new BigDecimal(despesa), DATA, TipoLancamento.DESPESA);

        var resumo = service.consultarResumo(MES);
        assertEquals(MES, resumo.mes());
        assertEquals(new BigDecimal(receita), resumo.receitas());
        assertEquals(new BigDecimal(despesa), resumo.despesas());
        assertEquals(new BigDecimal(saldo), resumo.saldo());
    }

    @Test
    void mesSemMovimentoRetornaTotaisZerados() {
        var resumo = service.consultarResumo(MES);
        assertEquals(BigDecimal.ZERO, resumo.receitas());
        assertEquals(BigDecimal.ZERO, resumo.despesas());
        assertEquals(BigDecimal.ZERO, resumo.saldo());
    }

    @Test
    void somaLancamentosSemIncluirOutrosMesesEAnos() {
        service.cadastrar("Receita 1", new BigDecimal("100.00"), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Receita 2", new BigDecimal("50.00"), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Despesa 1", new BigDecimal("10.00"), DATA, TipoLancamento.DESPESA);
        service.cadastrar("Despesa 2", new BigDecimal("20.00"), DATA, TipoLancamento.DESPESA);
        service.cadastrar("Outro mês", new BigDecimal("999.00"), DATA.plusMonths(1), TipoLancamento.RECEITA);
        service.cadastrar("Outro ano", new BigDecimal("999.00"), DATA.minusYears(1), TipoLancamento.DESPESA);

        var resumo = service.consultarResumo(MES);
        assertEquals(new BigDecimal("150.00"), resumo.receitas());
        assertEquals(new BigDecimal("30.00"), resumo.despesas());
        assertEquals(new BigDecimal("120.00"), resumo.saldo());
        assertEquals(6, service.listar().size());
    }

    @ParameterizedTest
    @CsvSource({"RECEITA,10.00,0,10.00", "DESPESA,0,10.00,-10.00"})
    void aceitaMesComSomenteUmTipo(TipoLancamento tipo, String receitas, String despesas, String saldo) {
        service.cadastrar("Teste", new BigDecimal("10.00"), DATA, tipo);
        var resumo = service.consultarResumo(MES);
        assertEquals(new BigDecimal(receitas), resumo.receitas());
        assertEquals(new BigDecimal(despesas), resumo.despesas());
        assertEquals(new BigDecimal(saldo), resumo.saldo());
    }

    @Test
    void rejeitaMesNulo() {
        assertThrows(IllegalArgumentException.class, () -> service.consultarResumo(null));
    }
}
