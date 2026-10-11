package com.example.controlevendas

import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
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
    private fun descendants(view: View): List<View> = listOf(view) +
        if (view is ViewGroup) (0 until view.childCount).flatMap { descendants(view.getChildAt(it)) } else emptyList()

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
