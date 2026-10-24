package br.com.azt1.despesas.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record SaldoMensal(YearMonth mes, BigDecimal saldo) {
}
