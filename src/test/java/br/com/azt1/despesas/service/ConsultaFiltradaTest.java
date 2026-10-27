package br.com.azt1.despesas.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.azt1.despesas.model.TipoLancamento;
import br.com.azt1.despesas.repository.LancamentoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsultaFiltradaTest {

    private LancamentoService service;
    private static final YearMonth MES = YearMonth.of(2026, 10);

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
        service.cadastrar("Mercado", new BigDecimal("10.00"), LocalDate.of(2026, 10, 26), TipoLancamento.DESPESA);
        service.cadastrar("Salário", new BigDecimal("100.00"), LocalDate.of(2026, 10, 26), TipoLancamento.RECEITA);
        service.cadastrar("Novembro", new BigDecimal("20.00"), LocalDate.of(2026, 11, 26), TipoLancamento.DESPESA);
    }

    @Test
    void semFiltrosListaTodos() {
        assertEquals(3, service.listar(null, null).size());
    }

    @Test
    void aceitaSomenteMes() {
        assertEquals(2, service.listar(MES, null).size());
    }

    @Test
    void aceitaSomenteTipo() {
        assertEquals(2, service.listar(null, TipoLancamento.DESPESA).size());
    }

    @Test
    void aceitaMesETipoJuntos() {
        var resultado = service.listar(MES, TipoLancamento.DESPESA);
        assertEquals(1, resultado.size());
        assertEquals("Mercado", resultado.get(0).getDescricao());
    }
}
