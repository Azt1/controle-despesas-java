package br.com.azt1.despesas.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.azt1.despesas.model.Lancamento;
import br.com.azt1.despesas.model.TipoLancamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LancamentoRepositoryTest {

    private LancamentoRepository repository;

    @BeforeEach
    void preparar() {
        repository = new LancamentoRepository();
    }

    private Lancamento lancamento(String descricao) {
        return new Lancamento(descricao, new BigDecimal("10.00"),
                LocalDate.of(2026, 10, 16), TipoLancamento.DESPESA);
    }

    @Test
    void comecaSemLancamentos() {
        assertTrue(repository.listar().isEmpty());
    }

    @Test
    void guardaLancamentosNaOrdemDoCadastro() {
        Lancamento primeiro = lancamento("Mercado");
        Lancamento segundo = lancamento("Transporte");

        repository.salvar(primeiro);
        repository.salvar(segundo);

        List<Lancamento> resultado = repository.listar();
        assertEquals(2, resultado.size());
        assertSame(primeiro, resultado.get(0));
        assertSame(segundo, resultado.get(1));
    }

    @Test
    void alterarListaRetornadaNaoApagaDados() {
        repository.salvar(lancamento("Mercado"));

        repository.listar().clear();

        assertEquals(1, repository.listar().size());
    }

    @Test
    void listaAnteriorNaoMudaComNovoCadastro() {
        List<Lancamento> anterior = repository.listar();

        repository.salvar(lancamento("Mercado"));

        assertTrue(anterior.isEmpty());
        assertEquals(1, repository.listar().size());
    }

    @Test
    void rejeitaLancamentoNulo() {
        assertThrows(IllegalArgumentException.class, () -> repository.salvar(null));
        assertTrue(repository.listar().isEmpty());
    }
}
