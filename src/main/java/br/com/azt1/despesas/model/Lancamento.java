package br.com.azt1.despesas.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Lancamento {

    private final String descricao;
    private final BigDecimal valor;
    private final LocalDate data;
    private final TipoLancamento tipo;

    public Lancamento(String descricao, BigDecimal valor, LocalDate data, TipoLancamento tipo) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição é obrigatória.");
        }
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
        if (data == null) {
            throw new IllegalArgumentException("A data é obrigatória.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo é obrigatório.");
        }

        this.descricao = descricao.strip();
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    public TipoLancamento getTipo() {
        return tipo;
    }
}
