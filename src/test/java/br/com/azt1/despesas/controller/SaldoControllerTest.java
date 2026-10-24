package br.com.azt1.despesas.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.azt1.despesas.model.TipoLancamento;
import br.com.azt1.despesas.repository.LancamentoRepository;
import br.com.azt1.despesas.service.LancamentoService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SaldoControllerTest {

    private LancamentoService service;
    private MockMvc api;
    private static final LocalDate DATA = LocalDate.of(2026, 10, 24);

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
        api = MockMvcBuilders.standaloneSetup(new LancamentoController(service))
                .setControllerAdvice(new TratamentoErros()).build();
    }

    @ParameterizedTest
    @CsvSource({"100.00,30.00,70.00", "30.00,100.00,-70.00", "10.00,10.00,0.00", "1.10,0.80,0.30"})
    void retornaSaldoDoMes(String receita, String despesa, double esperado) throws Exception {
        service.cadastrar("Receita", new BigDecimal(receita), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Despesa", new BigDecimal(despesa), DATA, TipoLancamento.DESPESA);

        api.perform(get("/api/lancamentos/saldo").param("mes", "2026-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mes").value("2026-10"))
                .andExpect(jsonPath("$.saldo").value(esperado));
        assertEquals(2, service.listar().size());
    }

    @Test
    void mesSemMovimentoRetornaZero() throws Exception {
        api.perform(get("/api/lancamentos/saldo").param("mes", "2026-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mes").value("2026-10"))
                .andExpect(jsonPath("$.saldo").value(0));
    }

    @Test
    void ignoraOutrosMesesEAnos() throws Exception {
        service.cadastrar("Outubro", new BigDecimal("100.00"), DATA, TipoLancamento.RECEITA);
        service.cadastrar("Novembro", new BigDecimal("999.00"), DATA.plusMonths(1), TipoLancamento.DESPESA);
        service.cadastrar("Outro ano", new BigDecimal("999.00"), DATA.minusYears(1), TipoLancamento.DESPESA);

        api.perform(get("/api/lancamentos/saldo").param("mes", "2026-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(100.00));
    }
}
