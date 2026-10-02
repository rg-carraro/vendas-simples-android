package com.example.controlevendas

/** Contrato de domínio; nenhuma tela conhece SKU, token ou API do Play Billing. */
interface EntitlementRepository {
    fun currentState(totalSales: Int): EntitlementState
    fun purchaseLifetime()
    fun restorePurchases()
}

/** Implementação local provisória enquanto o Billing Play não está integrado. */
class LocalEntitlementRepository(
    private val lifetimeEntitled: () -> Boolean = { false }
) : EntitlementRepository {
    private val gate = FreeSalesGate()

    override fun currentState(totalSales: Int): EntitlementState =
        gate.state(totalSales, lifetimeEntitled())

    override fun purchaseLifetime() {
        // O fluxo real será implementado pelo adaptador Google Play Billing.
    }

    override fun restorePurchases() {
        // O fluxo real será implementado pelo adaptador Google Play Billing.
    }
}

object LifetimeProduct {
    @Deprecated("Legado; use FullAccessProduct")
    const val PLANNED_PRICE_CENTS = 4_990L
    const val PRODUCT_TYPE = "one_time"
}

object FullAccessProduct {
    const val PRODUCT_ID = "full_access"
    const val PRODUCT_TYPE = "one_time"
}
