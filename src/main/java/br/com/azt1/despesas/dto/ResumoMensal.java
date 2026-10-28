package br.com.azt1.despesas.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record ResumoMensal(YearMonth mes, BigDecimal receitas, BigDecimal despesas, BigDecimal saldo) {
}
