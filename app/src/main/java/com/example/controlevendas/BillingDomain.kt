package com.example.controlevendas

enum class EntitlementState { UNKNOWN, FREE_WITHIN_LIMIT, FREE_LIMIT_REACHED, FULL_ACCESS, PENDING, UNAVAILABLE }
interface EntitlementRepository { fun currentState(totalSales: Int): EntitlementState; fun requestFullAccess(); fun restorePurchases() }
class BillingEntitlementRepository(private val gate: FreeSalesGate = FreeSalesGate()) : EntitlementRepository {
    @Volatile var billingState = EntitlementState.UNKNOWN
    override fun currentState(totalSales: Int) = when (billingState) { EntitlementState.FULL_ACCESS -> EntitlementState.FULL_ACCESS; EntitlementState.PENDING, EntitlementState.UNKNOWN, EntitlementState.UNAVAILABLE -> billingState; else -> gate.state(totalSales, false) }
    override fun requestFullAccess() {}
    override fun restorePurchases() {}
}
object FullAccessProduct { const val PRODUCT_ID = "full_access"; const val PURCHASE_OPTION_ID = "full-access"; const val PRODUCT_TYPE = "one_time" }
