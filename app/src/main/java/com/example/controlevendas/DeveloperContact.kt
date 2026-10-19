package com.example.controlevendas

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/** Conteúdo público do canal único de contato do aplicativo. */
object DeveloperContact {
    const val developerName = "RGCarraro"
    const val email = "rgcarraro@gmail.com"
    const val subject = "Vendas Simples - Contato com o desenvolvedor"
    const val menuLabel = "Sobre o aplicativo"
    const val appName = "Vendas Simples"
    const val subtitle = "Controle de Vendas"
    const val description = "Organize seus clientes, vendas, parcelas e pagamentos de maneira simples e prática, com seus dados disponíveis no próprio dispositivo."
    const val contactAction = "Falar com o desenvolvedor"
    const val privacyAction = "Política de privacidade"

    fun versionLabel(versionName: String?): String =
        "Versão ${versionName?.takeIf(String::isNotBlank) ?: "indisponível"}"

    fun message(versionName: String?): String = """Olá!

Gostaria de entrar em contato com o desenvolvedor do Vendas Simples.

Minha mensagem:

[Descreva aqui sua dúvida ou problema]

---

Aplicativo: Vendas Simples
Versão instalada: ${versionName?.takeIf(String::isNotBlank) ?: "indisponível"}"""

    fun mailtoUri(versionName: String?): String {
        val charset = StandardCharsets.UTF_8.name()
        val encodedSubject = URLEncoder.encode(subject, charset).replace("+", "%20")
        val encodedBody = URLEncoder.encode(message(versionName), charset).replace("+", "%20")
        return "mailto:$email?subject=$encodedSubject&body=$encodedBody"
    }
}
