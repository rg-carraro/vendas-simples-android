package com.example.controlevendas

import org.junit.Assert.*
import org.junit.Test

class ReleaseCompatibilityTest {
    @Test fun freeAllowsThirtySalesAndBlocksNextSale() {
        val gate = FreeSalesGate()
        (0..29).forEach { assertTrue(gate.canCreateSale(it, false)) }
        assertFalse(gate.canCreateSale(30, false))
        assertFalse(gate.canCreateSale(31, false))
    }
    @Test fun acquiredLicenseOverridesSalesLimit() {
        val repository = BillingEntitlementRepository()
        repository.billingState = EntitlementState.FULL_ACCESS
        assertEquals(EntitlementState.FULL_ACCESS, repository.currentState(31))
        assertTrue(FreeSalesGate().canCreateSale(100, true))
        assertEquals("full_access", FullAccessProduct.PRODUCT_ID)
        assertEquals("full-access", FullAccessProduct.PURCHASE_OPTION_ID)
    }
    @Test fun unavailableAndPendingBillingKeepTheirStates() {
        val repository = BillingEntitlementRepository()
        listOf(EntitlementState.UNKNOWN, EntitlementState.PENDING, EntitlementState.UNAVAILABLE).forEach {
            repository.billingState = it
            assertEquals(it, repository.currentState(30))
        }
    }
}
