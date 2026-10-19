package com.example.controlevendas

import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeveloperContactTest {
    @Test
    fun publicIdentityAndAboutContentAreExact() {
        assertEquals("RGCarraro", DeveloperContact.developerName)
        assertEquals("rgcarraro@gmail.com", DeveloperContact.email)
        assertEquals("Sobre o aplicativo", DeveloperContact.menuLabel)
        assertEquals("Vendas Simples", DeveloperContact.appName)
        assertEquals("Controle de Vendas", DeveloperContact.subtitle)
        assertEquals(
            "Organize seus clientes, vendas, parcelas e pagamentos de maneira simples e prática, com seus dados disponíveis no próprio dispositivo.",
            DeveloperContact.description
        )
    }

    @Test
    fun versionComesFromInstalledMetadataAndHasSafeFallback() {
        assertEquals("Versão 1.0.13", DeveloperContact.versionLabel("1.0.13"))
        assertEquals("Versão 1.0.12", DeveloperContact.versionLabel("1.0.12"))
        assertEquals("Versão indisponível", DeveloperContact.versionLabel(null))
        assertEquals("Versão indisponível", DeveloperContact.versionLabel(" "))
    }

    @Test
    fun mailtoContainsEditableContactTemplateAndNoBusinessData() {
        val uri = URI(DeveloperContact.mailtoUri("1.0.13"))
        assertEquals("mailto", uri.scheme)
        assertEquals("rgcarraro@gmail.com", uri.schemeSpecificPart.substringBefore('?'))
        val query = uri.schemeSpecificPart.substringAfter('?').split('&').associate { part ->
            val pair = part.split('=', limit = 2)
            URLDecoder.decode(pair[0], StandardCharsets.UTF_8.name()) to
                URLDecoder.decode(pair.getOrElse(1) { "" }, StandardCharsets.UTF_8.name())
        }
        assertEquals(DeveloperContact.subject, query["subject"])
        val body = query["body"].orEmpty()
        assertEquals(DeveloperContact.message("1.0.13"), body)
        assertTrue(body.contains("[Descreva aqui sua dúvida ou problema]"))
        assertTrue(body.contains("Aplicativo: Vendas Simples\nVersão instalada: 1.0.13"))
        listOf("Cliente:", "Telefone:", "Venda:", "Pagamento:", "backup", "token", "database", "log interno")
            .forEach { assertFalse("corpo contém dado privado: $it", body.contains(it, ignoreCase = true)) }
    }
}
