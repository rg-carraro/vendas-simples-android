package com.example.controlevendas

import android.app.Instrumentation
import android.content.Context
import android.content.res.Configuration
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.io.File

/** Exercita views reais sem salvar vendas ou alterar o banco operacional do AVD. */
class FinalUiInstrumentedTests(private val instrumentation: Instrumentation) {
    private lateinit var activity: MainActivity
    private lateinit var preferences: BusinessPreferences
    private lateinit var previous: BusinessPreferenceState

    private fun runOnMainSync(action: () -> Unit) {
        var failure: Throwable? = null
        instrumentation.runOnMainSync {
            try {
                action()
            } catch (error: Throwable) {
                failure = error
            }
        }
        failure?.let { throw it }
    }

    fun prepare() {
        preferences = BusinessPreferences(instrumentation.targetContext)
        previous = preferences.snapshot()
        preferences.restoreState(BusinessPreferenceState("", true))
        activity = instrumentation.startActivitySync(Intent(instrumentation.targetContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) as MainActivity
        instrumentation.waitForIdleSync()
    }

    fun close() {
        runOnMainSync { activity.finish() }
        preferences.restoreState(previous)
    }

    private fun contextForNightMode(night: Boolean): Context {
        val configuration = Configuration(activity.resources.configuration)
        val nightMode = if (night) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
        configuration.uiMode = (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or nightMode
        return activity.createConfigurationContext(configuration)
    }

    private fun relativeLuminance(color: Int): Double {
        fun linear(channel: Int): Double {
            val normalized = channel / 255.0
            return if (normalized <= 0.04045) normalized / 12.92
            else Math.pow((normalized + 0.055) / 1.055, 2.4)
        }
        return 0.2126 * linear(Color.red(color)) +
            0.7152 * linear(Color.green(color)) +
            0.0722 * linear(Color.blue(color))
    }

    private fun assertContrast(context: Context, textColor: Int, backgroundColor: Int, minimum: Double) {
        assertContrastValues(
            ContextCompat.getColor(context, textColor), ContextCompat.getColor(context, backgroundColor), minimum
        )
    }

    private fun assertContrastValues(textColor: Int, backgroundColor: Int, minimum: Double) {
        val first = relativeLuminance(textColor)
        val second = relativeLuminance(backgroundColor)
        val ratio = (maxOf(first, second) + 0.05) / (minOf(first, second) + 0.05)
        check(ratio >= minimum) { "Contraste ${"%.2f".format(ratio)}:1 abaixo de ${minimum}:1" }
    }

    private fun assertRenderedTokenText(actual: Int, token: Int, background: Int, label: String, alternateToken: Int? = null) {
        val actualRgb = Color.rgb(Color.red(actual), Color.green(actual), Color.blue(actual))
        val expectedRgb = listOfNotNull(token, alternateToken).map { Color.rgb(Color.red(it), Color.green(it), Color.blue(it)) }
        check(actualRgb in expectedRgb) {
            "$label não usa a cor semântica esperada"
        }
        assertContrastValues(ColorUtils.compositeColors(actual, background), background, 4.5)
    }

    private fun selectedColor(context: Context, resource: Int, vararg states: Int): Int =
        ContextCompat.getColorStateList(context, resource)!!.let { selector ->
            selector.getColorForState(states, selector.defaultColor)
        }

    fun testLightAndDarkSemanticColorContrast() = runOnMainSync {
        val light = contextForNightMode(night = false)
        val dark = contextForNightMode(night = true)
        check((light.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO)
        check((dark.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
        check(ContextCompat.getColor(light, R.color.color_app_background) != ContextCompat.getColor(dark, R.color.color_app_background))

        listOf(light, dark).forEach { context ->
            assertContrast(context, R.color.color_text_primary, R.color.color_app_background, 4.5)
            assertContrast(context, R.color.color_text_secondary, R.color.color_app_background, 4.5)
            assertContrast(context, R.color.color_text_primary, R.color.color_surface, 4.5)
            assertContrast(context, R.color.color_text_secondary, R.color.color_app_background, 4.5)
            assertContrast(context, R.color.color_text_secondary, R.color.color_surface, 4.5)
            assertContrast(context, R.color.color_text_disabled, R.color.color_surface, 4.5)
            assertContrast(context, R.color.color_text_secondary, R.color.color_surface_variant, 4.5)
            assertContrast(context, R.color.color_text_primary, R.color.color_surface_variant, 4.5)
            assertContrast(context, R.color.color_on_secondary, R.color.color_secondary, 4.5)
            assertContrast(context, R.color.color_on_primary, R.color.color_primary, 4.5)
            assertContrast(context, R.color.color_primary_variant, R.color.color_surface, 4.5)
            assertContrast(context, R.color.color_primary_variant, R.color.color_surface_variant, 4.5)
            assertContrast(context, R.color.color_primary, R.color.color_app_background, 4.5)
            assertContrast(context, R.color.color_text_primary, R.color.color_error_surface, 4.5)
            assertContrast(context, R.color.color_error_text, R.color.color_error_surface, 4.5)
            assertContrast(context, R.color.color_primary, R.color.color_surface, 4.5)
            assertContrast(context, R.color.color_field_outline, R.color.color_surface, 3.0)
            assertContrast(context, R.color.color_status_paid_text, R.color.color_status_paid_surface, 4.5)
            assertContrast(context, R.color.color_status_open_text, R.color.color_status_open_surface, 4.5)
            assertContrast(context, R.color.color_status_partial_text, R.color.color_status_partial_surface, 4.5)
            assertContrast(context, R.color.color_status_overdue_text, R.color.color_status_overdue_surface, 4.5)
            assertContrast(context, R.color.color_text_primary, R.color.color_status_paid_surface, 4.5)
            assertContrast(context, R.color.color_text_primary, R.color.color_status_open_surface, 4.5)
            assertContrast(context, R.color.color_text_primary, R.color.color_status_partial_surface, 4.5)
            assertContrast(context, R.color.color_text_primary, R.color.color_status_overdue_surface, 4.5)
            assertContrast(context, R.color.secondary_button_text, R.color.color_surface, 4.5)
            assertContrast(context, R.color.primary_button_text, R.color.color_primary, 4.5)

            val disabled = intArrayOf(-android.R.attr.state_enabled)
            val disabledSurface = ContextCompat.getColor(context, R.color.color_surface_variant)
            val disabledText = selectedColor(context, R.color.primary_button_text, *disabled)
            check(disabledText == ContextCompat.getColor(context, R.color.color_button_disabled_text))
            assertContrastValues(disabledText, disabledSurface, 4.5)
            assertContrastValues(selectedColor(context, R.color.secondary_button_text, *disabled), disabledSurface, 4.5)
            check(selectedColor(context, R.color.primary_button_background, *disabled) == disabledSurface)
            check(selectedColor(context, R.color.secondary_button_background, *disabled) == disabledSurface)
        }

        val focusedField = ContextCompat.getColorStateList(light, R.color.field_stroke)!!
        val focusedColor = focusedField.getColorForState(
            intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused), focusedField.defaultColor
        )
        check(focusedColor == ContextCompat.getColor(light, R.color.color_primary))
        val disabledFieldColor = focusedField.getColorForState(
            intArrayOf(-android.R.attr.state_enabled), focusedField.defaultColor
        )
        check(disabledFieldColor == ContextCompat.getColor(light, R.color.color_text_disabled))
        val darkFocusedField = ContextCompat.getColorStateList(dark, R.color.field_stroke)!!
        check(darkFocusedField.getColorForState(
            intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused), darkFocusedField.defaultColor
        ) == ContextCompat.getColor(dark, R.color.color_primary))
        val darkDisabledFieldColor = darkFocusedField.getColorForState(
            intArrayOf(-android.R.attr.state_enabled), darkFocusedField.defaultColor
        )
        check(darkDisabledFieldColor == ContextCompat.getColor(dark, R.color.color_text_disabled))
    }

    fun testMaterialDialogTextAndDisabledActionColors() = runOnMainSync {
        val titleText = "Personalize o negócio"
        val messageText = "Texto explicativo sintético para conferir o tema do diálogo."
        val dialog = MaterialAlertDialogBuilder(activity)
            .setTitle(titleText)
            .setMessage(messageText)
            .setPositiveButton("Salvar", null)
            .setNegativeButton("Cancelar", null)
            .create()
        try {
            dialog.show()
            val texts = descendants(dialog.window!!.decorView).filterIsInstance<TextView>()
            val title = texts.single { it.text.toString() == titleText }
            val message = texts.single { it.text.toString() == messageText }
            val primaryAction = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val surface = ContextCompat.getColor(activity, R.color.color_surface)
            assertRenderedTokenText(title.currentTextColor, ContextCompat.getColor(activity, R.color.color_text_primary), surface, "Título")
            assertRenderedTokenText(message.currentTextColor, ContextCompat.getColor(activity, R.color.color_text_secondary), surface, "Mensagem")
            check(primaryAction.currentTextColor == ContextCompat.getColor(activity, R.color.color_primary))
            primaryAction.isEnabled = false
            check(primaryAction.currentTextColor == ContextCompat.getColor(activity, R.color.color_text_disabled))
        } finally {
            dialog.dismiss()
        }
    }

    fun testMaterialDialogColorsFollowLightAndDarkResources() = runOnMainSync {
        val themedContext = activity
        val night = (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        run {
            val titleText = if (night) "Diálogo escuro sintético" else "Diálogo claro sintético"
            val messageText = "Mensagem de confirmação para validar contraste."
            val dialog = MaterialAlertDialogBuilder(themedContext)
                .setTitle(titleText)
                .setMessage(messageText)
                .setPositiveButton("Salvar", null)
                .setNegativeButton("Cancelar", null)
                .create()
            try {
                dialog.show()
                val texts = descendants(dialog.window!!.decorView).filterIsInstance<TextView>()
                val title = texts.single { it.text.toString() == titleText }
                val message = texts.single { it.text.toString() == messageText }
                val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                val surface = ContextCompat.getColor(themedContext, R.color.color_surface)
                assertRenderedTokenText(title.currentTextColor, ContextCompat.getColor(themedContext, R.color.color_text_primary), surface, "Título")
                assertRenderedTokenText(message.currentTextColor, ContextCompat.getColor(themedContext, R.color.color_text_secondary), surface, "Mensagem")
                check(positive.currentTextColor == ContextCompat.getColor(themedContext, R.color.color_primary))
                check(negative.currentTextColor == ContextCompat.getColor(themedContext, R.color.color_primary))
                positive.isEnabled = false
                check(positive.currentTextColor == ContextCompat.getColor(themedContext, R.color.color_text_disabled))
            } finally {
                dialog.dismiss()
            }
        }
    }

    private fun field(name: String) = MainActivity::class.java.getDeclaredField(name).apply { isAccessible = true }
    private fun call(name: String) = MainActivity::class.java.getDeclaredMethod(name).apply { isAccessible = true }.invoke(activity)

    fun testAboutScreenMenuIdentityVersionAndBackNavigation() = runOnMainSync {
        val createMenu = MainActivity::class.java.getDeclaredMethod("criarMenuOpcoes", View::class.java)
            .apply { isAccessible = true }
        val popup = createMenu.invoke(activity, View(activity)) as android.widget.PopupMenu
        val labels = (0 until popup.menu.size()).map { popup.menu.getItem(it).title.toString() }
        check(labels.containsAll(listOf("Personalizar negócio", "Exportar CSV", DeveloperContact.menuLabel)))
        check("Política de Privacidade" !in labels)
        call("abrirSobreAplicativo")

        val content = field("content").get(activity) as LinearLayout
        val texts = descendants(content).filterIsInstance<TextView>().map { it.text.toString() }
        val expected = listOf(
            DeveloperContact.appName,
            DeveloperContact.subtitle,
            DeveloperContact.versionLabel(activity.packageManager.getPackageInfo(activity.packageName, 0).versionName),
            DeveloperContact.description,
            "Desenvolvedor: RGCarraro",
            "E-mail: rgcarraro@gmail.com",
            DeveloperContact.contactAction,
            DeveloperContact.privacyAction
        )
        check(texts.containsAll(expected)) { "Faltam informações na tela Sobre: ${expected.filterNot { it in texts }}" }
        check(descendants(content).filterIsInstance<android.widget.ImageView>().any { it.drawable != null })
        check(field("telaAtual").get(activity) == "sobre")
        activity.onBackPressedDispatcher.onBackPressed()
        check(field("telaAtual").get(activity) == "menu")
        check((field("statusText").get(activity) as TextView).text == "Menu Principal")
    }

    fun testContactIntentAndMissingEmailClientFallback() = runOnMainSync {
        call("abrirSobreAplicativo")
        val intent = call("criarIntentContatoDesenvolvedor") as Intent
        check(intent.action == Intent.ACTION_SENDTO)
        check(intent.data?.scheme == "mailto")
        check(intent.data?.schemeSpecificPart?.substringBefore('?') == DeveloperContact.email)
        val query = Uri.parse("https://example.invalid/?${intent.data.toString().substringAfter('?')}")
        check(query.getQueryParameter("subject") == DeveloperContact.subject)
        check(query.getQueryParameter("body") == DeveloperContact.message(
            activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
        ))
        check(intent.extras == null)

        val execute = MainActivity::class.java.getDeclaredMethod("executarIntentContato", Function1::class.java)
            .apply { isAccessible = true }
        val fallback = execute.invoke(activity, { _: Intent -> throw android.content.ActivityNotFoundException() }) as AlertDialog
        try {
            check(fallback.isShowing)
            check(fallback.getButton(AlertDialog.BUTTON_POSITIVE).text == "Copiar e-mail")
        } finally { fallback.dismiss() }
    }

    fun testReportCallbackAndNotificationPermission() = runOnMainSync {
        var completed = false
        val callback: () -> Unit = { completed = true }
        MainActivity::class.java.getDeclaredMethod("carregarRelatorio", Function0::class.java)
            .apply { isAccessible = true }.invoke(activity, callback)
        check(completed)
        check((field("statusText").get(activity) as TextView).text.startsWith("Dados locais"))
        val cache = field("vendasCache")
        val previousSales = cache.get(activity)
        val manager = activity.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        try {
            manager.cancel(2026)
            val due = VendaRelatorio(1.0, nome_cliente = "Cliente sintético", data_vencimento = "2000-01-01",
                valor_total = 10000, total_pago = 2500, saldo = 7500)
            cache.set(activity, listOf(due))
            call("atualizarAvisosSemInterromperTela")
            val allowed = android.os.Build.VERSION.SDK_INT < 33 ||
                activity.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
            val deadline = android.os.SystemClock.elapsedRealtime() + 2000
            while (allowed && manager.activeNotifications.none { it.id == 2026 } && android.os.SystemClock.elapsedRealtime() < deadline) {
                android.os.SystemClock.sleep(25)
            }
            check(manager.activeNotifications.any { it.id == 2026 } == allowed)
            manager.cancel(2026)
            cache.set(activity, listOf(due.copy(total_pago = 10000, saldo = 0)))
            call("atualizarAvisosSemInterromperTela")
            check(manager.activeNotifications.none { it.id == 2026 })
        } finally {
            cache.set(activity, previousSales)
            manager.cancel(2026)
        }
    }

    fun testPrivacyPolicyFreeFullAndUnavailableOffline() = runOnMainSync {
        val repository = field("billingEntitlement").get(activity) as BillingEntitlementRepository
        val original = repository.billingState
        try {
            call("abrirSobreAplicativo")
            val content = field("content").get(activity) as View
            val privacyButton = descendants(content).filterIsInstance<Button>()
                .single { it.text.toString() == DeveloperContact.privacyAction }
            check(privacyButton.isEnabled && privacyButton.hasOnClickListeners())
            for (state in listOf(EntitlementState.FREE_WITHIN_LIMIT, EntitlementState.FULL_ACCESS, EntitlementState.UNAVAILABLE)) {
                call("abrirSobreAplicativo")
                val aboutContent = field("content").get(activity) as View
                check(descendants(aboutContent).filterIsInstance<Button>().any {
                    it.text.toString() == DeveloperContact.privacyAction && it.isEnabled
                })
                repository.billingState = state
                val dialog = call("abrirPoliticaPrivacidade") as AlertDialog
                try {
                    check(dialog.isShowing)
                    val texts = descendants(dialog.window!!.decorView).filterIsInstance<TextView>().joinToString("\n") { it.text }
                    check(texts.contains("Política de Privacidade") && texts.contains("Exclusão e retenção"))
                    check(texts.contains("não possui criptografia própria") && texts.contains("Google Play Billing"))
                    check(dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled)
                    check(dialog.getButton(AlertDialog.BUTTON_NEUTRAL).isEnabled)
                } finally { dialog.dismiss() }
            }
        } finally { repository.billingState = original }
    }

    fun testPdfIdentityAppearsOnlyOnFirstPageWithNameAndFallback() = runOnMainSync {
        val pdfMethod = MainActivity::class.java.getDeclaredMethod(
            "gerarPdfGenerico", File::class.java, String::class.java, List::class.java,
            Boolean::class.javaPrimitiveType!!, String::class.java
        ).apply { isAccessible = true }
        val previousName = preferences.snapshot()
        val sales = (1..14).map { index ->
            VendaRelatorio(index.toDouble(), nome_cliente = "Cliente $index", descricao = "Item $index",
                data_venda = "2026-10-01", data_vencimento = "2026-10-31", valor_total = 10000,
                total_pago = 2500, saldo = 7500, parcela_atual = 1, parcelas = 1)
        }
        fun assertBranding(file: File) {
            val descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            try {
                PdfRenderer(descriptor).use { renderer ->
                    check(renderer.pageCount > 1)
                    fun topCenterPixels(pageIndex: Int): Int {
                        renderer.openPage(pageIndex).use { page ->
                            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                            bitmap.eraseColor(Color.WHITE)
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            var colored = 0
                            val density = page.width / 595f
                            val left = (270 * density).toInt().coerceAtLeast(0)
                            val right = (325 * density).toInt().coerceAtMost(bitmap.width)
                            val top = (35 * density).toInt().coerceAtLeast(0)
                            val bottom = (88 * density).toInt().coerceAtMost(bitmap.height)
                            for (y in top until bottom) for (x in left until right) {
                                if (Color.red(bitmap.getPixel(x, y)) < 245 || Color.green(bitmap.getPixel(x, y)) < 245 || Color.blue(bitmap.getPixel(x, y)) < 245) colored++
                            }
                            bitmap.recycle()
                            return colored
                        }
                    }
                    check(topCenterPixels(0) > 20) { "Ícone não visível no cabeçalho da primeira página" }
                    check(topCenterPixels(1) == 0) { "Ícone/cabeçalho repetido na segunda página" }
                }
            } finally { descriptor.close() }
        }
        val file = File(activity.cacheDir, "ui-test-v14-identidade.pdf")
        try {
            preferences.businessName = "Loja São José — Acessórios"
            pdfMethod.invoke(activity, file, "Resumo Vendas Simples", sales, false, null)
            assertBranding(file)
            check(preferences.displayName == "Loja São José — Acessórios")
            file.delete()
            preferences.businessName = ""
            pdfMethod.invoke(activity, file, "Resumo Vendas Simples", sales, false, null)
            assertBranding(file)
            check(preferences.displayName == "Vendas Simples")
        } finally {
            file.delete()
            preferences.restoreState(previousName)
        }
    }
    private fun descendants(view: View): List<View> = listOf(view) +
        if (view is ViewGroup) (0 until view.childCount).flatMap { descendants(view.getChildAt(it)) } else emptyList()

    private fun renderSale(venda: VendaRelatorio): LinearLayout {
        val content = field("content").get(activity) as LinearLayout
        content.removeAllViews()
        MainActivity::class.java.getDeclaredMethod("adicionarCardVenda", VendaRelatorio::class.java)
            .apply { isAccessible = true }.invoke(activity, venda)
        return content.getChildAt(0) as LinearLayout
    }

    fun testSaleStatusesColorsAndResponsiveLayout() = runOnMainSync {
        val base = VendaRelatorio(1.0, nome_cliente = "Cliente João — " + "Nome longo ".repeat(12),
            data_venda = "2026-10-03", data_vencimento = "2999-12-27", data_pagamento = "2026-10-12",
            valor_total = 10000, total_pago = 0, saldo = 10000, parcela_atual = 15, parcelas = 15)
        val cases = listOf(
            Triple(base, "EM ABERTO", R.color.color_status_open_surface),
            Triple(base.copy(total_pago = 2500, saldo = 7500), "PAGO PARCIALMENTE", R.color.color_status_partial_surface),
            Triple(base.copy(data_vencimento = "2000-01-01"), "VENCIDO", R.color.color_status_overdue_surface),
            Triple(base.copy(data_vencimento = "2000-01-01", total_pago = 2500, saldo = 7500), "VENCIDO", R.color.color_status_overdue_surface),
            Triple(base.copy(data_vencimento = "2000-01-01", total_pago = 10000, saldo = 0), "QUITADO", R.color.color_status_paid_surface),
            Triple(base.copy(total_pago = 15000, saldo = -5000), "QUITADO", R.color.color_status_paid_surface),
            Triple(base.copy(valor_total = 0, saldo = 0), "QUITADO", R.color.color_status_paid_surface),
            Triple(base.copy(valor_total = Long.MAX_VALUE, total_pago = 1, saldo = Long.MAX_VALUE - 1), "PAGO PARCIALMENTE", R.color.color_status_partial_surface))
        val density = activity.resources.displayMetrics.density
        cases.forEach { (sale, expectedStatus, backgroundResource) ->
            val card = renderSale(sale)
            val status = descendants(card).filterIsInstance<TextView>().single { it.text.toString() == expectedStatus }
            check((status.background as GradientDrawable).color!!.defaultColor == ContextCompat.getColor(activity, backgroundResource))
            val foregroundResource = when (expectedStatus) {
                "QUITADO" -> R.color.color_status_paid_text
                "VENCIDO" -> R.color.color_status_overdue_text
                "PAGO PARCIALMENTE" -> R.color.color_status_partial_text
                else -> R.color.color_status_open_text
            }
            check(status.currentTextColor == ContextCompat.getColor(activity, foregroundResource))
            check(card.isClickable && card.hasOnClickListeners())
            val initialWidth = (288 * density).toInt()
            card.measure(View.MeasureSpec.makeMeasureSpec(initialWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            card.layout(0, 0, initialWidth, card.measuredHeight)
            val texts = descendants(card).filterIsInstance<TextView>()
            check(texts.any { it.text.toString() == "Parcela 15/15" })
            check(texts.any { it.text.toString() == "Compra: 03/10/2026 | Vencimento: ${BrazilianDates.display(sale.data_vencimento!!)}" })
            check(texts.any { it.text.toString() == "Data Pagamento: 12/10/2026" } == (sale.total_pago > 0))
            val values = descendants(card).filterIsInstance<ClientReportValues>().single()
            val valueTexts = descendants(values).filterIsInstance<TextView>()
            listOf(sale.valor_total, sale.total_pago, sale.saldo).forEachIndexed { index, cents ->
                val value = valueTexts[index * 2 + 1]
                check(value.text.toString() == FinancialChartPercentage.money(cents))
                check(value.typeface.isBold == (index == 2))
            }
            listOf(288, 608).forEach { width ->
                val pixels = (width * density).toInt()
                card.measure(View.MeasureSpec.makeMeasureSpec(pixels, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                card.layout(0, 0, pixels, card.measuredHeight)
                texts.forEach { text ->
                    check(text.ellipsize == null)
                    check(text.layout.getLineEnd(text.layout.lineCount - 1) == text.text.length)
                    (0 until text.layout.lineCount).forEach { line ->
                        val available = text.width - text.paddingLeft - text.paddingRight
                        check(text.layout.getLineMax(line) <= available + 1f) {
                            "Texto excede largura: ${text.text}, linha=$line, visivel=${text.layout.getLineMax(line)}, " +
                                "comEspacos=${text.layout.getLineWidth(line)}, disponivel=$available, card=${width}dp"
                        }
                    }
                }
                val header = card.getChildAt(0) as SaleCardHeader
                val name = header.getChildAt(0)
                check(if (header.orientation == LinearLayout.HORIZONTAL) status.left >= name.right else status.top >= name.bottom)
                check(status.right <= header.width)
            }
        }
        val short = renderSale(base.copy(nome_cliente = "João"))
        short.measure(View.MeasureSpec.makeMeasureSpec((328 * density).toInt(), View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        if (activity.resources.configuration.fontScale <= 1.3f) check((short.getChildAt(0) as SaleCardHeader).orientation == LinearLayout.HORIZONTAL)
    }

    fun testSalePaymentRefreshAndFinancialIntegrity() = runOnMainSync {
        val context = BackupTestContext(activity)
        context.deleteDatabase(LocalDatabase.DB_NAME)
        val database = LocalDatabase(context)
        val original = field("localDb").get(activity)
        try {
            field("localDb").set(activity, database)
            database.novaVenda(NovaVendaRequest("Cliente sintético", "Três parcelas", "2999-01-01", 10000, 3), emptyList())
            val sales = database.getRelatorio()
            check(sales.size == 3 && sales.sumOf { it.valor_total } == 10000L)
            val sale = sales.first()
            val id = sale.id_venda.toLong().toString()
            database.novoPagamento(NovoPagamentoRequest(id, "2026-10-12", 1000))
            val partial = database.getRelatorio().single { it.id_venda == sale.id_venda }
            check(partial.total_pago == 1000L && partial.saldo == partial.valor_total - 1000L)
            check(descendants(renderSale(partial)).filterIsInstance<TextView>().any { it.text.toString() == "PAGO PARCIALMENTE" })
            database.novoPagamento(NovoPagamentoRequest(id, "2026-10-12", partial.saldo))
            val before = database.getRelatorio()
            val paid = before.single { it.id_venda == sale.id_venda }
            check(paid.total_pago == paid.valor_total && paid.saldo == 0L)
            check(descendants(renderSale(paid)).filterIsInstance<TextView>().any { it.text.toString() == "QUITADO" })
            check(database.getRelatorio() == before)
            check(before.sumOf { it.valor_total } == 10000L)
            check(before.sumOf { it.total_pago } + before.sumOf { it.saldo } == 10000L)
        } finally {
            field("localDb").set(activity, original)
            database.close()
            context.deleteDatabase(LocalDatabase.DB_NAME)
        }
    }

    fun testSaleFiltersAndOrdering() = runOnMainSync {
        val sales = listOf(
            VendaRelatorio(1.0, nome_cliente = "João", descricao = "Brinco", data_venda = "2026-10-03", data_vencimento = "2999-01-01", valor_total = 10000, total_pago = 1000, saldo = 9000),
            VendaRelatorio(2.0, nome_cliente = "Maria", descricao = "Colar", data_venda = "2026-09-01", data_vencimento = "2999-03-01", valor_total = 10000, total_pago = 10000, saldo = 0),
            VendaRelatorio(3.0, nome_cliente = "Ana", descricao = "Anel", data_venda = "2026-10-12", data_vencimento = "2999-02-01", valor_total = 10000, total_pago = 0, saldo = 10000))
        field("vendasCache").set(activity, sales)
        fun names(): List<String> {
            call("abrirListaVendas")
            return descendants(field("content").get(activity) as View).filterIsInstance<SaleCardHeader>()
                .map { (it.getChildAt(0) as TextView).text.toString() }
        }
        check(names() == listOf("Maria", "Ana", "João"))
        field("filtroCliente").set(activity, "JOÃO")
        check(names() == listOf("João"))
        field("filtroCliente").set(activity, "anel")
        check(names() == listOf("Ana"))
        field("filtroCliente").set(activity, "")
        field("filtrarAReceber").set(activity, true)
        check(names() == listOf("Ana", "João"))
        field("filtroInicio").set(activity, "2026-10-10")
        field("filtroFim").set(activity, "2026-10-31")
        check(names() == listOf("Ana"))
        check(sales.sumOf { it.valor_total } == 30000L && sales.sumOf { it.total_pago } == 11000L && sales.sumOf { it.saldo } == 19000L)
    }

    fun testHeaderWithoutBusinessName() = runOnMainSync {
        call("atualizarCabecalhoNegocio")
        val texts = descendants(activity.window.decorView).filterIsInstance<TextView>().map { it.text.toString() }
        check("Vendas Simples" in texts && "Seu controle de vendas" in texts)
        check((field("businessTitle").get(activity) as TextView).visibility == View.GONE)
    }

    fun testHeaderWithAccentsAndLongName() = runOnMainSync {
        val name = "Acessório & Joias — São José " + "Á".repeat(90)
        preferences.businessName = name
        call("atualizarCabecalhoNegocio")
        val label = field("businessTitle").get(activity) as TextView
        check(label.text.toString() == BusinessIdentity.normalize(name))
        check(label.visibility == View.VISIBLE && label.maxLines == 1)
        check(label.contentDescription.toString().contains(label.text))
        check(descendants(activity.window.decorView).filterIsInstance<TextView>().any { it.text.toString() == "Vendas Simples" })
    }

    fun testPreferenceSurvivesNewActivity() {
        preferences.restoreState(BusinessPreferenceState("Acessório & Joias — São José", true))
        runOnMainSync { activity.finish() }
        activity = instrumentation.startActivitySync(Intent(instrumentation.targetContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) as MainActivity
        instrumentation.waitForIdleSync()
        runOnMainSync {
            check((field("businessTitle").get(activity) as TextView).text.toString() == "Acessório & Joias — São José")
        }
    }

    fun testMainMenuHasNoConfigurationButton() = runOnMainSync {
        call("abrirMenuPrincipal")
        check(descendants(field("content").get(activity) as View).filterIsInstance<Button>()
            .none { it.text.toString().contains("Configurações") })
    }

    fun testClientValuesLayoutAndBold() = runOnMainSync {
        val density = activity.resources.displayMetrics.density
        listOf(listOf("R$ 2.000,00", "R$ 138,00", "R$ 1.862,00"),
            listOf("R$ 92.233.720.368.547.758,07", "R$ 0,01", "R$ 92.233.720.368.547.758,06")).forEach { values ->
            val view = ClientReportValues(activity, values[0], values[1], values[2], Color.BLACK, Color.DKGRAY)
            listOf(288, 328, 608).forEach { width ->
                val pixels = (width * density).toInt()
                view.measure(View.MeasureSpec.makeMeasureSpec(pixels, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                view.layout(0, 0, pixels, view.measuredHeight)
                val labels = descendants(view).filterIsInstance<TextView>()
                values.forEachIndexed { index, value ->
                    val text = labels.single { it.text.toString() == value }
                    check(text.typeface.isBold == (index == 2))
                    check(text.ellipsize == null)
                    check(text.layout.lineCount >= 1 && (0 until text.layout.lineCount).all { text.layout.getEllipsisCount(it) == 0 })
                    check(text.width <= pixels)
                }
                check(labels.filter { it.text.toString() in listOf("Vendido", "Recebido", "Faltante") }.none { it.typeface.isBold })
                if (width == 328 && values[0] == "R$ 2.000,00" && activity.resources.configuration.fontScale <= 1.3f) {
                    check(view.childCount == 1 && (view.getChildAt(0) as LinearLayout).childCount == 3)
                }
            }
        }
        val name = "Cliente João — " + "Nome longo com acentos ".repeat(8)
        field("vendasCache").set(activity, listOf(VendaRelatorio(1.0, nome_cliente = name,
            valor_total = 200000, total_pago = 13800, saldo = 186200)))
        call("abrirResumoClientes")
        val content = field("content").get(activity) as View
        check(descendants(content).filterIsInstance<TextView>().any { it.text.toString() == "$name — 1 venda" && it.ellipsize == null })
        field("vendasCache").set(activity, listOf(
            VendaRelatorio(1.0, nome_cliente = name, valor_total = 200000, total_pago = 13800, saldo = 186200),
            VendaRelatorio(2.0, nome_cliente = name, valor_total = 200000, total_pago = 13800, saldo = 186200)))
        call("abrirResumoClientes")
        check(descendants(content).filterIsInstance<TextView>().any { it.text.toString() == "$name — 2 vendas" })
    }

    fun testFinanceMonthAndTotals() = runOnMainSync {
        field("vendasCache").set(activity, listOf(
            VendaRelatorio(1.0, nome_cliente = "João", data_venda = "2026-10-11", valor_total = 10000, total_pago = 5000, saldo = 5000),
            VendaRelatorio(2.0, nome_cliente = "Maria", data_venda = "2026-09-11", valor_total = 3500, total_pago = 500, saldo = 3000)))
        listOf("2026-10" to listOf("R$ 100,00", "R$ 50,00", "R$ 100,00 · 100,0%", "R$ 50,00 · 50,0%"),
            "2026-09" to listOf("R$ 35,00", "R$ 5,00", "R$ 30,00", "R$ 5,00 · 14,3%", "R$ 30,00 · 85,7%"),
            "2026-08" to listOf("R$ 0,00", "R$ 0,00 · 0,0%")).forEach { (month, values) ->
            field("mesDashboardSelecionado").set(activity, month)
            call("abrirDashboardFinanceiro")
            val texts = descendants(field("content").get(activity) as View).filterIsInstance<TextView>()
                .map { it.text.toString().replace('\u00A0', ' ') }
            check(values.all { it in texts })
            check("$month ▾" in texts)
            val chart = descendants(field("content").get(activity) as View).filterIsInstance<FinancialChartLabel>()
            check(chart.size == 3)
            if (month == "2026-08") check(chart.all { descendants(it).filterIsInstance<TextView>().any {
                label -> label.text.toString().replace('\u00A0', ' ') == "R$ 0,00 · 0,0%"
            } })
        }
    }

    fun testFinancialChartLabelsResponsive() = runOnMainSync {
        val density = activity.resources.displayMetrics.density
        listOf("R$ 3.050,00 · 100,0%", "R$ 238,00 · 7,8%", "R$ 0,00 · 0,0%",
            "R$ 92.233.720.368.547.758,07 · 100,0%").forEach { value ->
            listOf(268, 288, 608).forEach { width ->
                val view = FinancialChartLabel(activity, "A Receber", value, Color.BLACK)
                val pixels = (width * density).toInt()
                view.measure(View.MeasureSpec.makeMeasureSpec(pixels, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                view.layout(0, 0, pixels, view.measuredHeight)
                val texts = descendants(view).filterIsInstance<TextView>()
                check(texts.map { it.text.toString() } == listOf("A Receber", value))
                texts.forEach { text ->
                    check(text.ellipsize == null && text.width <= pixels)
                    check(text.layout.getLineEnd(text.layout.lineCount - 1) == text.text.length)
                    check((0 until text.layout.lineCount).all { text.layout.getEllipsisCount(it) == 0 })
                    check((0 until text.layout.lineCount).all { text.layout.getLineWidth(it) <= text.width + 1f })
                }
                check(if (view.orientation == LinearLayout.HORIZONTAL) texts[1].left >= texts[0].right
                    else texts[1].top >= texts[0].bottom)
                if (width == 608 && value == "R$ 238,00 · 7,8%" && activity.resources.configuration.fontScale <= 1.3f) {
                    check(view.orientation == LinearLayout.HORIZONTAL)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun testAddAndRemoveFirstLastAndOnlyPhoto() = runOnMainSync {
        val photos = field("fotosRascunho").get(activity) as MutableList<ByteArray>
        val list = LinearLayout(activity)
        field("listaFotosRascunho").set(activity, list)
        photos.clear()
        val add = MainActivity::class.java.getDeclaredMethod("adicionarFotoRascunho", Uri::class.java).apply { isAccessible = true }
        listOf(Color.RED, Color.GREEN, Color.BLUE).forEachIndexed { index, color ->
            val file = File(activity.cacheDir, "ui-test-photo-$index.png")
            val bitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888).apply { eraseColor(color) }
            try {
                file.outputStream().use { check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) }
                add.invoke(activity, Uri.fromFile(file))
            } finally { bitmap.recycle(); file.delete() }
        }
        check(photos.size == 3 && list.childCount == 3)
        val second = photos[1]
        val third = photos[2]
        fun remove(index: Int) {
            val row = list.getChildAt(index) as LinearLayout
            val button = row.getChildAt(1) as Button
            check(button.contentDescription.toString() == "Remover foto ${index + 1} de ${photos.size}")
            val params = button.layoutParams as LinearLayout.LayoutParams
            check(params.width == LinearLayout.LayoutParams.WRAP_CONTENT)
            val gap = (16 * activity.resources.displayMetrics.density).toInt()
            check(if (row.orientation == LinearLayout.HORIZONTAL) params.leftMargin == gap else params.topMargin == gap)
            button.performClick()
        }
        remove(0)
        check(photos.size == 2 && photos[0] === second && photos[1] === third)
        remove(1)
        check(photos.size == 1 && photos[0] === second)
        remove(0)
        check(photos.isEmpty() && list.childCount == 0)
        field("listaFotosRascunho").set(activity, null)
    }
}
