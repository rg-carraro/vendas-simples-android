package com.example.controlevendas

import java.math.BigDecimal

/** CSV brasileiro: UTF-8 explícito, delimitador ; e registros terminados por CRLF. */
object SalesCsvExporter {
    private val headers = listOf("Cliente", "Descrição", "Data Compra", "Vencimento", "Valor", "Pago", "Faltante", "Parcela")

    fun encode(sales: List<VendaRelatorio>, withBom: Boolean = false): ByteArray = buildString {
        if (withBom) append('\uFEFF')
        appendRecord(headers)
        sales.forEach { sale ->
            appendRecord(listOf(
                sale.nome_cliente.orEmpty(), sale.descricao.orEmpty(),
                BrazilianDates.display(sale.data_venda.orEmpty()),
                BrazilianDates.display(sale.data_vencimento.orEmpty()),
                money(sale.valor_total), money(sale.total_pago), money(sale.saldo),
                "${sale.parcela_atual ?: 1}/${sale.parcelas ?: 1}"
            ))
        }
    }.toByteArray(Charsets.UTF_8)

    private fun money(cents: Long): String = BigDecimal.valueOf(cents, 2).toPlainString().replace('.', ',')

    private fun StringBuilder.appendRecord(fields: List<String>) {
        append(fields.joinToString(";") { field ->
            if (field.any { it == ';' || it == ',' || it == '"' || it == '\r' || it == '\n' }) {
                "\"${field.replace("\"", "\"\"")}\""
            } else field
        })
        append("\r\n")
    }
}
