package com.example.controlevendas

import org.junit.Assert.assertEquals
import org.junit.Test

class FinancialChartPercentageTest {
    @Test fun moneyKeepsEveryCent() {
        assertEquals("R$ 92.233.720.368.547.758,07", FinancialChartPercentage.money(Long.MAX_VALUE).replace('\u00A0', ' '))
        assertEquals("R$ 0,01", FinancialChartPercentage.money(1).replace('\u00A0', ' '))
        assertEquals("R$ 3.050,00", FinancialChartPercentage.money(305000).replace('\u00A0', ' '))
    }
    @Test fun normalValues() {
        assertEquals("100,0%", FinancialChartPercentage.format(305000, 305000))
        assertEquals("7,8%", FinancialChartPercentage.format(23800, 305000))
        assertEquals("92,2%", FinancialChartPercentage.format(281200, 305000))
    }

    @Test fun zeroDenominator() {
        listOf(0L, 1L, Long.MAX_VALUE).forEach {
            assertEquals("0,0%", FinancialChartPercentage.format(it, 0))
        }
    }

    @Test fun extremeValuesWithoutOverflow() {
        assertEquals("100,0%", FinancialChartPercentage.format(Long.MAX_VALUE, Long.MAX_VALUE))
        assertEquals("922337203685477580700,0%", FinancialChartPercentage.format(Long.MAX_VALUE, 1))
        assertEquals("0,0%", FinancialChartPercentage.format(1, Long.MAX_VALUE))
    }

    @Test fun inconsistentTotalsAreNotForced() {
        assertEquals("120,0%", FinancialChartPercentage.format(120, 100))
        assertEquals("-20,0%", FinancialChartPercentage.format(-20, 100))
        assertEquals("33,3%", FinancialChartPercentage.format(1, 3))
        assertEquals("0,1%", FinancialChartPercentage.format(1, 2000))
        assertEquals("100,0%", FinancialChartPercentage.format(1999, 2000))
    }
}
