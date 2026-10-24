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

class MesInvalidoTest {

    private LancamentoService service;
    private MockMvc api;
    private static final String MENSAGEM = "Informe o mês no formato yyyy-MM, por exemplo 2026-10.";

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
        service.cadastrar("Teste", new BigDecimal("10.00"),
                LocalDate.of(2026, 10, 24), TipoLancamento.RECEITA);
        api = MockMvcBuilders.standaloneSetup(new LancamentoController(service))
                .setControllerAdvice(new TratamentoErros()).build();
    }

    @Test
    void exigeMesNaConsulta() throws Exception {
        api.perform(get("/api/lancamentos/saldo"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(MENSAGEM));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "abc", "2026-00", "2026-13", "10/2026", "2026-10-24"})
    void rejeitaMesInvalidoSemAlterarLancamentos(String mes) throws Exception {
        api.perform(get("/api/lancamentos/saldo").param("mes", mes))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(MENSAGEM));

        assertEquals(1, service.listar().size());
        assertEquals(new BigDecimal("10.00"), service.listar().get(0).getValor());
    }
}
