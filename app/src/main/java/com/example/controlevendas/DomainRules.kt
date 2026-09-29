package com.example.controlevendas

/** Valor monetário inteiro em centavos. Não usa Float ou Double. */
@JvmInline
value class MoneyCents(val value: Long) {
    init {
        require(value >= 0) { "Valor monetário não pode ser negativo" }
    }

    operator fun plus(other: MoneyCents): MoneyCents =
        MoneyCents(Math.addExact(value, other.value))

    operator fun minus(other: MoneyCents): MoneyCents =
        MoneyCents(Math.subtractExact(value, other.value))

    fun formatBrazilian(): String {
        val reais = value / 100
        val centavos = (value % 100).toString().padStart(2, '0')
        return "R$ ${reais.toString().reversed().chunked(3).joinToString(".").reversed()},$centavos"
    }
}

enum class EntitlementState {
    FREE_WITHIN_LIMIT,
    FREE_LIMIT_REACHED,
    LIFETIME_ENTITLED,
    PENDING,
    UNAVAILABLE
}

/** Regra local do MVP: somente a criação da 31ª venda é bloqueada. */
class FreeSalesGate(private val freeLimit: Int = FREE_SALES_LIMIT) {
    init {
        require(freeLimit > 0) { "O limite FREE deve ser positivo" }
    }

    fun state(totalSales: Int, lifetimeEntitled: Boolean): EntitlementState {
        require(totalSales >= 0) { "A quantidade de vendas não pode ser negativa" }
        return when {
            lifetimeEntitled -> EntitlementState.LIFETIME_ENTITLED
            totalSales < freeLimit -> EntitlementState.FREE_WITHIN_LIMIT
            else -> EntitlementState.FREE_LIMIT_REACHED
        }
    }

    fun canCreateSale(totalSales: Int, lifetimeEntitled: Boolean): Boolean =
        state(totalSales, lifetimeEntitled) != EntitlementState.FREE_LIMIT_REACHED

    companion object {
        const val FREE_SALES_LIMIT = 30
    }
}
