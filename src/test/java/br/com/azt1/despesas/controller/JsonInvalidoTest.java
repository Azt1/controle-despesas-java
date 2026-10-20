package br.com.azt1.despesas.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.azt1.despesas.repository.LancamentoRepository;
import br.com.azt1.despesas.service.LancamentoService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JsonInvalidoTest {

    private LancamentoService service;
    private MockMvc api;

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
        api = MockMvcBuilders.standaloneSetup(new LancamentoController(service))
                .setControllerAdvice(new TratamentoErros()).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{", "", "null", "[]",
            "{\"descricao\":\"Teste\",\"valor\":10,\"data\":\"2026-10-20\",\"tipo\":\"OUTRO\"}",
            "{\"descricao\":\"Teste\",\"valor\":10,\"data\":\"20/10/2026\",\"tipo\":\"DESPESA\"}",
            "{\"descricao\":\"Teste\",\"valor\":10,\"data\":\"2026-02-30\",\"tipo\":\"DESPESA\"}",
            "{\"descricao\":\"Teste\",\"valor\":\"abc\",\"data\":\"2026-10-20\",\"tipo\":\"DESPESA\"}"
    })
    void rejeitaCorpoInvalidoSemSalvar(String corpo) throws Exception {
        api.perform(post("/api/lancamentos").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem")
                        .value("Envie um JSON válido, com data no formato yyyy-MM-dd e tipo RECEITA ou DESPESA."));

        assertTrue(service.listar().isEmpty());
    }
}
