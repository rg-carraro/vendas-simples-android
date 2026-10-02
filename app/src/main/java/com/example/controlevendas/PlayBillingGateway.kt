package com.example.controlevendas

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.PurchasesUpdatedListener

/**
 * Infraestrutura mínima do Google Play Billing. A UI e o domínio continuam
 * dependentes apenas de EntitlementRepository; nenhuma compra é iniciada aqui.
 */
class PlayBillingGateway(context: Context) {
    companion object {
        const val FULL_ACCESS_PRODUCT_ID = "full_access"
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { _, _ ->
        // O processamento de compras será ligado ao repositório de entitlement
        // em uma etapa posterior, com validação de estados pendente/cancelado.
    }

    private val client: BillingClient = BillingClient.newBuilder(context.applicationContext)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    fun connect(listener: BillingClientStateListener) {
        client.startConnection(listener)
    }

    fun disconnect() {
        client.endConnection()
    }

    fun isReady(): Boolean = client.isReady
}
