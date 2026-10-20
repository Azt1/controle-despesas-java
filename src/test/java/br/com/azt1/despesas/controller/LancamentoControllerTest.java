package br.com.azt1.despesas.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.azt1.despesas.model.TipoLancamento;
import br.com.azt1.despesas.repository.LancamentoRepository;
import br.com.azt1.despesas.service.LancamentoService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LancamentoControllerTest {

    private LancamentoService service;
    private MockMvc api;

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
        api = MockMvcBuilders.standaloneSetup(new LancamentoController(service)).build();
    }

    @Test
    void retornaListaVazia() throws Exception {
        api.perform(get("/api/lancamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void retornaLancamentoCadastrado() throws Exception {
        service.cadastrar("Mercado", new BigDecimal("42.50"),
                LocalDate.of(2026, 10, 20), TipoLancamento.DESPESA);

        api.perform(get("/api/lancamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].descricao").value("Mercado"))
                .andExpect(jsonPath("$[0].valor").value(42.50))
                .andExpect(jsonPath("$[0].data").value("2026-10-20"))
                .andExpect(jsonPath("$[0].tipo").value("DESPESA"));
    }
}
