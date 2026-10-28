package br.com.azt1.despesas.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.azt1.despesas.model.TipoLancamento;
import br.com.azt1.despesas.repository.LancamentoRepository;
import br.com.azt1.despesas.service.LancamentoService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResumoControllerTest {

    private LancamentoService service;
    private MockMvc api;

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
        api = MockMvcBuilders.standaloneSetup(new LancamentoController(service))
                .setControllerAdvice(new TratamentoErros()).build();
    }

    @Test
    void retornaTotaisESaldoDoMes() throws Exception {
        service.cadastrar("Receita", new BigDecimal("100.00"), LocalDate.of(2026, 10, 28), TipoLancamento.RECEITA);
        service.cadastrar("Compra", new BigDecimal("30.50"), LocalDate.of(2026, 10, 28), TipoLancamento.DESPESA);
        service.cadastrar("Outro mês", new BigDecimal("999.00"), LocalDate.of(2026, 11, 28), TipoLancamento.DESPESA);

        api.perform(get("/api/lancamentos/resumo").param("mes", "2026-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mes").value("2026-10"))
                .andExpect(jsonPath("$.receitas").value(100.00))
                .andExpect(jsonPath("$.despesas").value(30.50))
                .andExpect(jsonPath("$.saldo").value(69.50));
        assertEquals(3, service.listar().size());
    }

    @Test
    void retornaZeroEmMesSemMovimento() throws Exception {
        api.perform(get("/api/lancamentos/resumo").param("mes", "2026-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mes").value("2026-10"))
                .andExpect(jsonPath("$.receitas").value(0))
                .andExpect(jsonPath("$.despesas").value(0))
                .andExpect(jsonPath("$.saldo").value(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/lancamentos/resumo", "/api/lancamentos/resumo?mes=",
            "/api/lancamentos/resumo?mes=2026-13", "/api/lancamentos/resumo?mes=outubro",
            "/api/lancamentos/resumo?mes=28/10/2026"})
    void exigeMesValido(String rota) throws Exception {
        api.perform(get(rota))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Informe o mês no formato yyyy-MM, por exemplo 2026-10."));
    }
}
