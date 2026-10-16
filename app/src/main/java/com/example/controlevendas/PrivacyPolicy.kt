package com.example.controlevendas

import java.net.URI

/** Cópia local gratuita e URL GitHub Pages verificada por HTTPS em 15/10/2026. */
object PrivacyPolicy {
    const val officialUrl = "https://rg-carraro.github.io/vendas-simples-android/privacy/"
    fun safeWebUrl(value: String?): String? = runCatching {
        val uri = URI(value ?: return null)
        value.takeIf { uri.scheme.equals("https", true) && !uri.host.isNullOrBlank() && uri.rawUserInfo == null }
    }.getOrNull()

    fun openWeb(value: String?, launch: (String) -> Unit): Boolean {
        val url = safeWebUrl(value) ?: return false
        return try { launch(url); true } catch (_: Exception) { false }
    }
}
