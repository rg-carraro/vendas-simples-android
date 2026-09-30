package com.example.controlevendas

data class VendaRelatorio(
    val id_venda: Double,
    val id_cliente: String? = null,
    val nome_cliente: String? = null,
    val descricao: String? = null,
    val data_venda: String? = null,
    val data_vencimento: String? = null,
    val data_pagamento: String? = null,
    val valor_total: Long,
    val total_pago: Long,
    val saldo: Long,
    val parcela_atual: Int? = null,
    val parcelas: Int? = null,
    val id_venda_pai: String? = null
)

data class NovaVendaRequest(
    val nome_cliente: String,
    val descricao: String,
    val data_venda: String,
    val valor_total: Long,
    val parcelas: Int
)

data class AtualizarVendaRequest(
    val id_venda: String,
    val nome_cliente: String,
    val descricao: String,
    val data_venda: String,
    val data_vencimento: String,
    val valor_total: Long,
    val parcelas: Int,
    val parcela_atual: Int
)

data class NovoPagamentoRequest(
    val id_venda: String,
    val data_pagamento: String,
    val valor_pago: Long
)

data class DeletarVendaRequest(
    val id_venda: String
)
