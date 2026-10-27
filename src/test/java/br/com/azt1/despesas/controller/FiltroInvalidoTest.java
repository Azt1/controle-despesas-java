package br.com.azt1.despesas.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.azt1.despesas.repository.LancamentoRepository;
import br.com.azt1.despesas.service.LancamentoService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FiltroInvalidoTest {

    private MockMvc api;

    @BeforeEach
    void preparar() {
        var service = new LancamentoService(new LancamentoRepository());
        api = MockMvcBuilders.standaloneSetup(new LancamentoController(service))
                .setControllerAdvice(new TratamentoErros()).build();
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
            /api/lancamentos?tipo=OUTRO | Informe o tipo como RECEITA ou DESPESA.
            /api/lancamentos?tipo=despesa | Informe o tipo como RECEITA ou DESPESA.
            /api/lancamentos?tipo=1 | Informe o tipo como RECEITA ou DESPESA.
            /api/lancamentos?mes=2026-10&tipo=OUTRO | Informe o tipo como RECEITA ou DESPESA.
            /api/lancamentos?mes=2026-13 | Informe o mês no formato yyyy-MM, por exemplo 2026-10.
            /api/lancamentos?mes=2026-00 | Informe o mês no formato yyyy-MM, por exemplo 2026-10.
            /api/lancamentos?mes=outubro | Informe o mês no formato yyyy-MM, por exemplo 2026-10.
            /api/lancamentos?mes=2026-10-26&tipo=DESPESA | Informe o mês no formato yyyy-MM, por exemplo 2026-10.
            """)
    void retornaOrientacaoParaFiltroInvalido(String rota, String mensagem) throws Exception {
        api.perform(get(rota))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(mensagem));
    }
}
