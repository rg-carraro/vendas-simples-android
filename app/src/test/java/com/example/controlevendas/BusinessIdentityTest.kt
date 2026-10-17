package com.example.controlevendas

import org.junit.Assert.assertEquals
import org.junit.Test

class BusinessIdentityTest {
    @Test fun emptyIdentityUsesGenericPresentation() {
        assertEquals("Vendas Simples", BusinessIdentity.displayName("  "))
        assertEquals("Obrigado pela preferência!", BusinessIdentity.thanks(""))
        assertEquals("Resumo de vendas — Vendas Simples", BusinessIdentity.summary(""))
    }
    @Test fun accentedNameHasOneSignatureAndNoInjectedLines() {
        assertEquals("Obrigado pela preferência!\n\nJoão & Jóias", BusinessIdentity.thanks("  João &\nJóias  "))
    }
    @Test fun longNameIsBoundedWithoutLosingAccents() {
        val name = "Á".repeat(120)
        assertEquals(name, BusinessIdentity.normalize(name + "B"))
        assertEquals(name, BusinessIdentity.displayName(name))
    }
    @Test fun datesRoundTripWithoutChangingStoredDates() {
        listOf("2026-10-11", "2024-02-29", "2027-01-01").forEach {
            assertEquals(it, BrazilianDates.storage(BrazilianDates.display(it)))
        }
        assertEquals("11/10/2026", BrazilianDates.display("2026-10-11"))
        assertEquals("2026-10-11", BrazilianDates.storage("2026-10-11"))
    }
    @Test fun missingAndAlreadyFormattedDatesArePreserved() {
        listOf("", "-", "Não informada", "11/10/2026").forEach {
            assertEquals(it, BrazilianDates.display(it))
        }
    }
}
