package br.com.azt1.despesas.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
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

class FiltroControllerTest {

    private LancamentoService service;
    private MockMvc api;

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
        salvar("Mercado", "2026-10-26", TipoLancamento.DESPESA);
        salvar("Salário", "2026-10-26", TipoLancamento.RECEITA);
        salvar("Novembro", "2026-11-26", TipoLancamento.DESPESA);
        salvar("Outro ano", "2025-10-26", TipoLancamento.DESPESA);
        api = MockMvcBuilders.standaloneSetup(new LancamentoController(service))
                .setControllerAdvice(new TratamentoErros()).build();
    }

    private void salvar(String descricao, String data, TipoLancamento tipo) {
        service.cadastrar(descricao, new BigDecimal("10.00"), LocalDate.parse(data), tipo);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
            /api/lancamentos | 4
            /api/lancamentos?mes=2026-10 | 2
            /api/lancamentos?tipo=DESPESA | 3
            /api/lancamentos?tipo=RECEITA | 1
            /api/lancamentos?mes=2026-10&tipo=DESPESA | 1
            /api/lancamentos?mes=2026-11&tipo=RECEITA | 0
            /api/lancamentos?mes=2025-10&tipo=DESPESA | 1
            """)
    void consultaComFiltrosSemAlterarDados(String rota, int quantidade) throws Exception {
        api.perform(get(rota))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(quantidade));
        assertEquals(4, service.listar().size());
    }
}
