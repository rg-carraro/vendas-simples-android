package com.example.controlevendas

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

/** Percentual de apresentação; não altera nem reconcilia os totais financeiros. */
object FinancialChartPercentage {
    fun money(value: Long): String = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        .format(BigDecimal.valueOf(value, 2))

    fun format(value: Long, sold: Long): String = if (sold <= 0L) "0,0%" else
        BigDecimal.valueOf(value).multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(sold), 1, RoundingMode.HALF_UP)
            .toPlainString().replace('.', ',') + "%"
}
