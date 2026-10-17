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

    @Test fun accentsUtf8AndDates() {
        val bytes = SalesCsvExporter.encode(listOf(sale()))
        assertArrayEquals("Cliente".toByteArray(Charsets.US_ASCII), bytes.take(7).toByteArray())
        assertTrue(text(sale()).contains("Descrição"))
        assertTrue(text(sale()).contains("João;brinco;11/10/2026;05/11/2026;"))
    }

    @Test fun escapesUserTextWithoutLoss() {
        assertTrue(text(sale(name = "tutu; Silva", description = "brinco, \"pérola\"\r\nsegunda\nlinha\rfim"))
            .contains("\"tutu; Silva\";\"brinco, \"\"pérola\"\"\r\nsegunda\nlinha\rfim\";"))
    }

    @Test fun emptyReportAndMissingFields() {
        assertEquals("Cliente;Descrição;Data Compra;Vencimento;Valor;Pago;Faltante;Parcela\r\n", text())
        assertTrue(text(sale(name = null, description = null)).contains("\r\n;;11/10/2026;"))
    }

    @Test fun standardParserFixtureWithInstallments() {
        val sales = listOf(
            sale(name = "tutu; João — 漢字 😀", description = "brinco, \"pérola\"\r\nsegunda\nlinha\rfim"),
            sale(3333, 1000, part = 1, parts = 3),
            sale(3333, 0, part = 2, parts = 3),
            sale(3334, 3334, part = 3, parts = 3),
            sale(0, 0), sale(1, 0), sale(Long.MAX_VALUE, 0)
        )
        val bytes = SalesCsvExporter.encode(sales)
        assertTrue(bytes.isNotEmpty())
        File("build/test-artifacts/sales-v11.csv").apply { parentFile?.mkdirs(); writeBytes(bytes) }
        File("build/test-artifacts/sales-v11-excel.csv").writeBytes(SalesCsvExporter.encode(sales, withBom = true))
    }

    @Test fun bomIsOptionalAndOnlyChangesPrefix() {
        val standard = SalesCsvExporter.encode(listOf(sale()))
        val excel = SalesCsvExporter.encode(listOf(sale()), withBom = true)
        assertArrayEquals(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()), excel.take(3).toByteArray())
        assertArrayEquals(standard, excel.drop(3).toByteArray())
        assertFalse(standard.toString(Charsets.UTF_8).startsWith("\uFEFF"))
    }

    @Test fun wrongDecoderReproducesReportedSymptoms() {
        val excel = SalesCsvExporter.encode(emptyList(), withBom = true)
        val wrong = excel.toString(Charsets.ISO_8859_1)
        assertTrue(wrong.startsWith("ï»¿Cliente"))
        assertTrue(wrong.contains("DescriÃ§Ã£o"))
        assertEquals("Descrição", excel.toString(Charsets.UTF_8).split(';')[1])
        assertArrayEquals(byteArrayOf(0x44, 0x65, 0x73, 0x63, 0x72, 0x69, 0xC3.toByte(), 0xA7.toByte(), 0xC3.toByte(), 0xA3.toByte(), 0x6F),
            "Descrição".toByteArray(Charsets.UTF_8))
    }

    @Test fun preservesAllUnicodeAndDoesNotReplaceUserText() {
        val name = "João — 漢字 😀 ï»¿ DescriÃ§Ã£o"
        assertTrue(text(sale(name = name)).contains(name))
        assertTrue(SalesCsvExporter.encode(listOf(sale(name = name)), true).toString(Charsets.UTF_8).contains(name))
    }
}
