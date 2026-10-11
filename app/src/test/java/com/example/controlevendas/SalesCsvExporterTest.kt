package com.example.controlevendas

import java.io.File
import org.junit.Assert.*
import org.junit.Test

class SalesCsvExporterTest {
    private fun sale(value: Long = 12345, paid: Long = 2345, name: String? = "João", description: String? = "brinco", part: Int? = null, parts: Int? = null) =
        VendaRelatorio(1.0, nome_cliente = name, descricao = description,
            data_venda = "2026-10-11", data_vencimento = "2026-11-05",
            valor_total = value, total_pago = paid, saldo = value - paid,
            parcela_atual = part, parcelas = parts)

    private fun text(vararg sales: VendaRelatorio) = SalesCsvExporter.encode(sales.toList()).toString(Charsets.UTF_8)

    @Test fun exactCentsAndPartialPayment() {
        assertTrue(text(sale()).contains("\"123,45\";\"23,45\";\"100,00\";1/1\r\n"))
        assertTrue(text(sale(1, 0)).contains("\"0,01\";\"0,00\";\"0,01\""))
        assertTrue(text(sale(0, 0)).contains("\"0,00\";\"0,00\";\"0,00\""))
    }

    @Test fun longPrecisionAndNegativeBalance() {
        assertTrue(text(sale(Long.MAX_VALUE, 0)).contains("\"92233720368547758,07\""))
        assertTrue(text(sale(0, 1)).contains("\"-0,01\""))
    }

    @Test fun accentsBomAndDates() {
        val bytes = SalesCsvExporter.encode(listOf(sale()))
        assertArrayEquals(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()), bytes.take(3).toByteArray())
        assertTrue(text(sale()).contains("Descrição"))
        assertTrue(text(sale()).contains("João;brinco;11/10/2026;05/11/2026;"))
    }

    @Test fun escapesUserTextWithoutLoss() {
        assertTrue(text(sale(name = "tutu; Silva", description = "brinco, \"pérola\"\r\nsegunda\nlinha\rfim"))
            .contains("\"tutu; Silva\";\"brinco, \"\"pérola\"\"\r\nsegunda\nlinha\rfim\";"))
    }

    @Test fun emptyReportAndMissingFields() {
        assertEquals("\uFEFFCliente;Descrição;Data Compra;Vencimento;Valor;Pago;Faltante;Parcela\r\n", text())
        assertTrue(text(sale(name = null, description = null)).contains("\r\n;;11/10/2026;"))
    }

    @Test fun standardParserFixtureWithInstallments() {
        val sales = listOf(
            sale(name = "tutu; João", description = "brinco, \"pérola\"\r\nsegunda\nlinha\rfim"),
            sale(3333, 1000, part = 1, parts = 3),
            sale(3333, 0, part = 2, parts = 3),
            sale(3334, 3334, part = 3, parts = 3),
            sale(0, 0), sale(1, 0), sale(Long.MAX_VALUE, 0)
        )
        val bytes = SalesCsvExporter.encode(sales)
        assertTrue(bytes.isNotEmpty())
        File("build/test-artifacts/sales-v11.csv").apply { parentFile?.mkdirs(); writeBytes(bytes) }
    }
}
