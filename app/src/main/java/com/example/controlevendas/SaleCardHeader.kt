package com.example.controlevendas

import android.content.Context
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.math.ceil

/** Recebe a etiqueta pronta: nenhuma classificação ou cor é definida aqui. */
class SaleCardHeader(context: Context, private val name: TextView, private val status: TextView) : LinearLayout(context) {
    private val gap = dp(8)
    private var horizontalMode: Boolean? = null

    init {
        gravity = Gravity.TOP
        isBaselineAligned = false
        addView(name)
        addView(status)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val available = View.MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val statusWidth = ceil(Layout.getDesiredWidth(status.text, status.paint).toDouble()).toInt() +
            status.paddingLeft + status.paddingRight
        val readableNameWidth = (dp(120) * resources.configuration.fontScale).toInt()
        val nameWidth = ceil(Layout.getDesiredWidth(name.text, name.paint).toDouble()).toInt().coerceAtMost(readableNameWidth)
        val horizontal = available >= nameWidth + statusWidth + gap
        if (horizontalMode != horizontal) {
            orientation = if (horizontal) HORIZONTAL else VERTICAL
            name.layoutParams = LayoutParams(if (horizontal) 0 else LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, if (horizontal) 1f else 0f)
            status.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.END or Gravity.TOP
                if (horizontal) leftMargin = gap else topMargin = dp(2)
            }
            horizontalMode = horizontal
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}
