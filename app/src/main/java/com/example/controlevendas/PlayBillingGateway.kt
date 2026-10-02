package com.example.controlevendas
import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
class PlayBillingGateway(context: Context, private val events: Events) : PurchasesUpdatedListener {
    interface Events { fun onState(state: EntitlementState); fun onProduct(details: ProductDetails?); fun onMessage(message: String) }
    private var product: ProductDetails? = null
    private val client = BillingClient.newBuilder(context.applicationContext).setListener(this).enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()).build()
    fun connect() { client.startConnection(object : BillingClientStateListener { override fun onBillingSetupFinished(r: BillingResult) { if (r.responseCode == BillingClient.BillingResponseCode.OK) { queryProduct(); restorePurchases() } else events.onState(EntitlementState.UNAVAILABLE) }; override fun onBillingServiceDisconnected() { events.onState(EntitlementState.UNKNOWN) } }) }
    fun disconnect() = client.endConnection()
    fun buy(a: Activity) { val d = product ?: run { events.onMessage("Produto indisponível no Google Play."); return }; client.launchBillingFlow(a, BillingFlowParams.newBuilder().setProductDetailsParamsList(listOf(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(d).build())).build()) }
    fun queryProduct() { if (!client.isReady) return; val q = QueryProductDetailsParams.newBuilder().setProductList(listOf(QueryProductDetailsParams.Product.newBuilder().setProductId(FullAccessProduct.PRODUCT_ID).setProductType(BillingClient.ProductType.INAPP).build())).build(); client.queryProductDetailsAsync(q) { r, d -> product = d.productDetailsList.firstOrNull(); events.onProduct(product); if (r.responseCode != BillingClient.BillingResponseCode.OK) events.onMessage("Não foi possível consultar o produto.") } }
    fun restorePurchases() { if (!client.isReady) return; client.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()) { r, p -> if (r.responseCode == BillingClient.BillingResponseCode.OK) handle(p) else events.onState(EntitlementState.UNKNOWN) } }
    override fun onPurchasesUpdated(r: BillingResult, p: MutableList<Purchase>?) { if (r.responseCode == BillingClient.BillingResponseCode.OK && p != null) handle(p) else if (r.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) events.onState(EntitlementState.FREE_WITHIN_LIMIT) else if (r.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) restorePurchases() else events.onMessage("Não foi possível concluir a compra.") }
    private fun handle(p: List<Purchase>) { val x = p.firstOrNull { it.products.contains(FullAccessProduct.PRODUCT_ID) }; when { x == null -> events.onState(EntitlementState.FREE_WITHIN_LIMIT); x.purchaseState == Purchase.PurchaseState.PENDING -> events.onState(EntitlementState.PENDING); x.purchaseState == Purchase.PurchaseState.PURCHASED -> { if (!x.isAcknowledged) client.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(x.purchaseToken).build()) { a -> if (a.responseCode != BillingClient.BillingResponseCode.OK) events.onMessage("Compra pendente de reconhecimento.") }; events.onState(EntitlementState.FULL_ACCESS) } } }
}

