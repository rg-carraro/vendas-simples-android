package com.example.controlevendas

/** Regras de apresentação; não altera registros financeiros. */
object BusinessIdentity {
    const val MAX_NAME_LENGTH = 120
    fun normalize(name: String): String = name.replace(Regex("\\s+"), " ").trim().take(MAX_NAME_LENGTH)
    fun displayName(name: String): String = normalize(name).ifEmpty { "Vendas Simples" }
    fun thanks(name: String): String = "Obrigado pela preferência!" +
        normalize(name).takeIf { it.isNotEmpty() }?.let { "\n\n$it" }.orEmpty()
    fun summary(name: String): String = "Resumo de vendas — ${displayName(name)}"
}

object BrazilianDates {
    private val iso = Regex("^(\\d{4})-(\\d{2})-(\\d{2})$")
    private val brazilian = Regex("^(\\d{2})/(\\d{2})/(\\d{4})$")
    fun display(value: String): String = iso.matchEntire(value)?.destructured?.let {
        (year, month, day) -> "$day/$month/$year"
    } ?: value
    fun storage(value: String): String = brazilian.matchEntire(value)?.destructured?.let {
        (day, month, year) -> "$year-$month-$day"
    } ?: value
}
