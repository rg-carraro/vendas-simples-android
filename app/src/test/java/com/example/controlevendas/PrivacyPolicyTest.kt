package com.example.controlevendas

import java.io.File
import org.junit.Assert.*
import org.junit.Test

class PrivacyPolicyTest {
    @Test fun missingBrowserAndRejectedUrlSelectLocalFallback() {
        assertFalse(PrivacyPolicy.openWeb(null) { fail() })
        assertFalse(PrivacyPolicy.openWeb("file:///private") { fail() })
        assertFalse(PrivacyPolicy.openWeb("https://example.invalid/privacy") { throw IllegalStateException("synthetic browser unavailable") })
        assertTrue(PrivacyPolicy.openWeb("https://example.invalid/privacy") { assertTrue(it.startsWith("https://")) })
    }
    @Test fun onlyAllowsHttpsWithoutEmbeddedCredentials() {
        assertEquals("https://rg-carraro.github.io/vendas-simples-android/privacy/", PrivacyPolicy.officialUrl)
        assertEquals(PrivacyPolicy.officialUrl, PrivacyPolicy.safeWebUrl(PrivacyPolicy.officialUrl))
        assertNull(PrivacyPolicy.safeWebUrl(null))
        listOf("http://example.invalid", "javascript:alert(1)", "file:///data/local", "https://user:pass@example.invalid", "https:///missing-host")
            .forEach { assertNull(PrivacyPolicy.safeWebUrl(it)) }
        assertEquals("https://example.invalid/privacy/", PrivacyPolicy.safeWebUrl("https://example.invalid/privacy/"))
    }
    @Test fun offlinePolicyMatchesLocalSiteRevisionAndContainsDeletionAndContact() {
        val root = if (File("src/main/assets").exists()) File(".") else File("app")
        val site = root.resolve("../site/privacy/index.html").readText(Charsets.UTF_8)
        val asset = root.resolve("src/main/assets/privacy_policy.html").readText(Charsets.UTF_8)
        assertEquals(site.substringAfter("<main>").substringBefore("</main>"), asset)
        listOf("Exclusão e retenção", "não possui criptografia própria", "Google Play Billing", "mailto:").forEach { assertTrue(asset.contains(it)) }
    }
}
