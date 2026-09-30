package com.example.controlevendas

import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyRulesTest {
    @Test fun centavo() = assertEquals("R$ 0,01", MoneyCents(1).formatBrazilian())
    @Test fun lifetimePrice() = assertEquals("R$ 49,90", MoneyCents(4990).formatBrazilian())

    @Test fun divideHundredByThreePreservesTotal() {
        val parts = MoneyRules.splitEvenly(10_000, 3)
        assertEquals(listOf(3333L, 3333L, 3334L), parts)
        assertEquals(10_000L, parts.sum())
    }

    @Test fun partialPaymentAndBalance() {
        val sale = MoneyCents(4990)
        val paid = MoneyCents(2000)
        assertEquals(MoneyCents(2990), sale - paid)
    }

    @Test fun paymentCorrectionAndZeroBalance() {
        val sale = MoneyCents(10_000)
        val corrected = MoneyCents(10_000)
        assertEquals(MoneyCents(0), sale - corrected)
    }

    @Test fun v2MigrationConversionUsesRoundedCents() {
        fun migrateLegacyReal(value: Double): Long = kotlin.math.round(value * 100.0).toLong()
        assertEquals(1L, migrateLegacyReal(0.01))
        assertEquals(4990L, migrateLegacyReal(49.90))
        assertEquals(3333L, migrateLegacyReal(33.33))
    }
}
