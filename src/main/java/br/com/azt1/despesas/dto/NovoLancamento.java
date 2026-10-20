package br.com.azt1.despesas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.azt1.despesas.model.TipoLancamento;

public record NovoLancamento(String descricao, BigDecimal valor, LocalDate data, TipoLancamento tipo) {
}
