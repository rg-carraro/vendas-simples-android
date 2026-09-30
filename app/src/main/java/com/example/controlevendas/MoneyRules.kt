package com.example.controlevendas

/** Regras determinísticas para dividir centavos sem perder o total. */
object MoneyRules {
    fun splitEvenly(totalCents: Long, parts: Int): List<Long> {
        require(totalCents >= 0)
        require(parts > 0)
        val base = totalCents / parts
        val remainder = (totalCents % parts).toInt()
        return List(parts) { index -> base + if (index >= parts - remainder) 1 else 0 }
    }
}
