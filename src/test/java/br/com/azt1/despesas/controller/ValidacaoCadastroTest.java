package br.com.azt1.despesas.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.azt1.despesas.repository.LancamentoRepository;
import br.com.azt1.despesas.service.LancamentoService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ValidacaoCadastroTest {

    private LancamentoService service;
    private MockMvc api;

    @BeforeEach
    void preparar() {
        service = new LancamentoService(new LancamentoRepository());
        api = MockMvcBuilders.standaloneSetup(new LancamentoController(service))
                .setControllerAdvice(new TratamentoErros()).build();
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
            {"valor":10,"data":"2026-10-20","tipo":"DESPESA"} | A descrição é obrigatória.
            {"descricao":" ","valor":10,"data":"2026-10-20","tipo":"DESPESA"} | A descrição é obrigatória.
            {"descricao":"Teste","data":"2026-10-20","tipo":"DESPESA"} | O valor deve ser maior que zero.
            {"descricao":"Teste","valor":0,"data":"2026-10-20","tipo":"DESPESA"} | O valor deve ser maior que zero.
            {"descricao":"Teste","valor":-10,"data":"2026-10-20","tipo":"DESPESA"} | O valor deve ser maior que zero.
            {"descricao":"Teste","valor":10,"tipo":"DESPESA"} | A data é obrigatória.
            {"descricao":"Teste","valor":10,"data":"2026-10-20"} | O tipo é obrigatório.
            """)
    void rejeitaDadosInvalidosSemSalvar(String corpo, String mensagem) throws Exception {
        api.perform(post("/api/lancamentos").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(mensagem));

        assertTrue(service.listar().isEmpty());
    }
}
