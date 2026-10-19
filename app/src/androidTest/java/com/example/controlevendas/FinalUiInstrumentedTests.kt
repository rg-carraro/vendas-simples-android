package com.example.controlevendas

import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
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

    fun prepare() {
        preferences = BusinessPreferences(instrumentation.targetContext)
        previous = preferences.snapshot()
        preferences.restoreState(BusinessPreferenceState("", true))
        activity = instrumentation.startActivitySync(Intent(instrumentation.targetContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) as MainActivity
        instrumentation.waitForIdleSync()
    }

    fun close() {
        instrumentation.runOnMainSync { activity.finish() }
        preferences.restoreState(previous)
    }

    private fun field(name: String) = MainActivity::class.java.getDeclaredField(name).apply { isAccessible = true }
    private fun call(name: String) = MainActivity::class.java.getDeclaredMethod(name).apply { isAccessible = true }.invoke(activity)

    fun testAboutScreenMenuIdentityVersionAndBackNavigation() = instrumentation.runOnMainSync {
        val createMenu = MainActivity::class.java.getDeclaredMethod("criarMenuOpcoes", View::class.java)
            .apply { isAccessible = true }
        val popup = createMenu.invoke(activity, View(activity)) as android.widget.PopupMenu
        val labels = (0 until popup.menu.size()).map { popup.menu.getItem(it).title.toString() }
        check(labels.containsAll(listOf("Personalizar negócio", "Política de Privacidade", "Exportar CSV", DeveloperContact.menuLabel)))
        check(popup.menu.performIdentifierAction(
            (0 until popup.menu.size()).map { popup.menu.getItem(it) }.single { it.title == DeveloperContact.menuLabel }.itemId,
            0
        ))

        val content = field("content").get(activity) as LinearLayout
        val texts = descendants(content).filterIsInstance<TextView>().map { it.text.toString() }
        check(texts.containsAll(listOf(
            DeveloperContact.appName,
            DeveloperContact.subtitle,
            DeveloperContact.versionLabel(activity.packageManager.getPackageInfo(activity.packageName, 0).versionName),
            DeveloperContact.description,
            "Desenvolvedor: RGCarraro",
            "E-mail: rgcarraro@gmail.com",
            DeveloperContact.contactAction,
            DeveloperContact.privacyAction
        )))
        check(descendants(content).filterIsInstance<android.widget.ImageView>().any { it.drawable != null })
        check(field("telaAtual").get(activity) == "sobre")
        activity.onBackPressedDispatcher.onBackPressed()
        check(field("telaAtual").get(activity) == "menu")
        check((field("statusText").get(activity) as TextView).text == "Menu Principal")
    }

    fun testContactIntentAndMissingEmailClientFallback() = instrumentation.runOnMainSync {
        call("abrirSobreAplicativo")
        val intent = call("criarIntentContatoDesenvolvedor") as Intent
        check(intent.action == Intent.ACTION_SENDTO)
        check(intent.data?.scheme == "mailto")
        check(intent.data?.schemeSpecificPart?.substringBefore('?') == DeveloperContact.email)
        check(intent.data?.getQueryParameter("subject") == DeveloperContact.subject)
        check(intent.data?.getQueryParameter("body") == DeveloperContact.message(
            activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
        ))
        check(intent.extras == null)

        val execute = MainActivity::class.java.getDeclaredMethod("executarIntentContato", Function1::class.java)
            .apply { isAccessible = true }
        val fallback = execute.invoke(activity, { _: Intent -> throw android.content.ActivityNotFoundException() }) as android.app.AlertDialog
        try {
            check(fallback.isShowing)
            check(fallback.getButton(android.app.AlertDialog.BUTTON_POSITIVE).text == "Copiar e-mail")
        } finally { fallback.dismiss() }
    }

    fun testReportCallbackAndNotificationPermission() = instrumentation.runOnMainSync {
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

    fun testPrivacyPolicyFreeFullAndUnavailableOffline() = instrumentation.runOnMainSync {
        val repository = field("billingEntitlement").get(activity) as BillingEntitlementRepository
        val original = repository.billingState
        try {
            for (state in listOf(EntitlementState.FREE_WITHIN_LIMIT, EntitlementState.FULL_ACCESS, EntitlementState.UNAVAILABLE)) {
                repository.billingState = state
                val dialog = call("abrirPoliticaPrivacidade") as android.app.AlertDialog
                try {
                    check(dialog.isShowing)
                    val texts = descendants(dialog.window!!.decorView).filterIsInstance<TextView>().joinToString("\n") { it.text }
                    check(texts.contains("Política de Privacidade") && texts.contains("Exclusão e retenção"))
                    check(texts.contains("não possui criptografia própria") && texts.contains("Google Play Billing"))
                    check(dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).isEnabled)
                    check(dialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL).isEnabled)
                } finally { dialog.dismiss() }
            }
        } finally { repository.billingState = original }
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

    fun testSaleStatusesColorsAndResponsiveLayout() = instrumentation.runOnMainSync {
        val base = VendaRelatorio(1.0, nome_cliente = "Cliente João — " + "Nome longo ".repeat(12),
            data_venda = "2026-10-03", data_vencimento = "2999-12-27", data_pagamento = "2026-10-12",
            valor_total = 10000, total_pago = 0, saldo = 10000, parcela_atual = 15, parcelas = 15)
        val cases = listOf(
            Triple(base, "EM ABERTO", Color.rgb(244, 229, 197)),
            Triple(base.copy(total_pago = 2500, saldo = 7500), "PAGO PARCIALMENTE", Color.rgb(252, 224, 184)),
            Triple(base.copy(data_vencimento = "2000-01-01"), "VENCIDO", Color.rgb(248, 205, 199)),
            Triple(base.copy(data_vencimento = "2000-01-01", total_pago = 2500, saldo = 7500), "VENCIDO", Color.rgb(248, 205, 199)),
            Triple(base.copy(data_vencimento = "2000-01-01", total_pago = 10000, saldo = 0), "QUITADO", Color.rgb(197, 231, 214)),
            Triple(base.copy(total_pago = 15000, saldo = -5000), "QUITADO", Color.rgb(197, 231, 214)),
            Triple(base.copy(valor_total = 0, saldo = 0), "QUITADO", Color.rgb(197, 231, 214)),
            Triple(base.copy(valor_total = Long.MAX_VALUE, total_pago = 1, saldo = Long.MAX_VALUE - 1), "PAGO PARCIALMENTE", Color.rgb(252, 224, 184)))
        val density = activity.resources.displayMetrics.density
        cases.forEach { (sale, expectedStatus, background) ->
            val card = renderSale(sale)
            val status = descendants(card).filterIsInstance<TextView>().single { it.text.toString() == expectedStatus }
            check((status.background as GradientDrawable).color!!.defaultColor == background)
            val foreground = when (expectedStatus) {
                "QUITADO" -> Color.rgb(30, 58, 95)
                "VENCIDO" -> Color.rgb(153, 55, 47)
                "PAGO PARCIALMENTE" -> Color.rgb(156, 82, 20)
                else -> Color.rgb(125, 87, 32)
            }
            check(status.currentTextColor == foreground)
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

    fun testSalePaymentRefreshAndFinancialIntegrity() = instrumentation.runOnMainSync {
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

    fun testSaleFiltersAndOrdering() = instrumentation.runOnMainSync {
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

    fun testHeaderWithoutBusinessName() = instrumentation.runOnMainSync {
        call("atualizarCabecalhoNegocio")
        val texts = descendants(activity.window.decorView).filterIsInstance<TextView>().map { it.text.toString() }
        check("Vendas Simples" in texts && "Seu controle de vendas" in texts)
        check((field("businessTitle").get(activity) as TextView).visibility == View.GONE)
    }

    fun testHeaderWithAccentsAndLongName() = instrumentation.runOnMainSync {
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
        instrumentation.runOnMainSync { activity.finish() }
        activity = instrumentation.startActivitySync(Intent(instrumentation.targetContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) as MainActivity
        instrumentation.waitForIdleSync()
        instrumentation.runOnMainSync {
            check((field("businessTitle").get(activity) as TextView).text.toString() == "Acessório & Joias — São José")
        }
    }

    fun testMainMenuHasNoConfigurationButton() = instrumentation.runOnMainSync {
        call("abrirMenuPrincipal")
        check(descendants(field("content").get(activity) as View).filterIsInstance<Button>()
            .none { it.text.toString().contains("Configurações") })
    }

    fun testClientValuesLayoutAndBold() = instrumentation.runOnMainSync {
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

    fun testFinanceMonthAndTotals() = instrumentation.runOnMainSync {
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

    fun testFinancialChartLabelsResponsive() = instrumentation.runOnMainSync {
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
    fun testAddAndRemoveFirstLastAndOnlyPhoto() = instrumentation.runOnMainSync {
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
