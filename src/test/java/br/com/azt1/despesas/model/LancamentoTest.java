package br.com.azt1.despesas.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LancamentoTest {

    private static final LocalDate DATA = LocalDate.of(2026, 10, 15);

    @ParameterizedTest
    @EnumSource(TipoLancamento.class)
    void guardaDadosDoLancamento(TipoLancamento tipo) {
        BigDecimal valor = new BigDecimal("120.50");

        Lancamento lancamento = new Lancamento("Lançamento de teste", valor, DATA, tipo);

        assertEquals("Lançamento de teste", lancamento.getDescricao());
        assertEquals(valor, lancamento.getValor());
        assertEquals(DATA, lancamento.getData());
        assertEquals(tipo, lancamento.getTipo());
    }

    @Test
    void removeEspacosNasPontasDaDescricao() {
        Lancamento lancamento = new Lancamento("  Conta de luz  ", new BigDecimal("90.00"), DATA,
                TipoLancamento.DESPESA);

        assertEquals("Conta de luz", lancamento.getDescricao());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void rejeitaDescricaoVazia(String descricao) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Lancamento(descricao, new BigDecimal("10.00"), DATA, TipoLancamento.DESPESA));

        assertEquals("A descrição é obrigatória.", erro.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.01", "10.00", "100000.99"})
    void aceitaValoresPositivos(String textoValor) {
        BigDecimal valor = new BigDecimal(textoValor);

        Lancamento lancamento = new Lancamento("Salário", valor, DATA, TipoLancamento.RECEITA);

        assertEquals(valor, lancamento.getValor());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.00", "-0.01", "-100"})
    void rejeitaValorZeroOuNegativo(String valor) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Lancamento("Compra", new BigDecimal(valor), DATA, TipoLancamento.DESPESA));

        assertEquals("O valor deve ser maior que zero.", erro.getMessage());
    }

    @Test
    void rejeitaValorNulo() {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Lancamento("Compra", null, DATA, TipoLancamento.DESPESA));

        assertEquals("O valor deve ser maior que zero.", erro.getMessage());
    }

    @Test
    void rejeitaDataNula() {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Lancamento("Compra", new BigDecimal("10.00"), null, TipoLancamento.DESPESA));

        assertEquals("A data é obrigatória.", erro.getMessage());
    }

    @Test
    void rejeitaTipoNulo() {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Lancamento("Compra", new BigDecimal("10.00"), DATA, null));

        assertEquals("O tipo é obrigatório.", erro.getMessage());
    }

    @Test
    void preservaPrecisaoDoValor() {
        BigDecimal valor = new BigDecimal("12345678901234567890.12");

        Lancamento lancamento = new Lancamento("Recebimento", valor, DATA, TipoLancamento.RECEITA);

        assertEquals(valor, lancamento.getValor());
    }
}
