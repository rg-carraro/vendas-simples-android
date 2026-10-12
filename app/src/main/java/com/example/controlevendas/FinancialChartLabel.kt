package com.example.controlevendas

import android.content.Context
import android.graphics.Typeface
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.math.ceil

/** Cabeçalho compacto que empilha apenas quando os textos não cabem juntos. */
class FinancialChartLabel(context: Context, label: String, value: String, color: Int) : LinearLayout(context) {
    private val gap = (8 * resources.displayMetrics.density).toInt()
    private var horizontalMode: Boolean? = null
    private val name = TextView(context).apply {
        text = label
        textSize = 13f
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(color)
    }
    private val amount = TextView(context).apply {
        text = value
        textSize = 13f
        gravity = Gravity.END
        setTextColor(color)
    }

    init {
        addView(name)
        addView(amount)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val available = View.MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val required = ceil(Layout.getDesiredWidth(name.text, name.paint).toDouble()).toInt() +
            ceil(Layout.getDesiredWidth(amount.text, amount.paint).toDouble()).toInt() + gap
        val horizontal = required <= available
        if (horizontalMode != horizontal) {
            orientation = if (horizontal) HORIZONTAL else VERTICAL
            name.layoutParams = LayoutParams(if (horizontal) 0 else LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, if (horizontal) 1f else 0f)
            amount.layoutParams = LayoutParams(if (horizontal) LayoutParams.WRAP_CONTENT else LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT).apply { if (horizontal) leftMargin = gap }
            horizontalMode = horizontal
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
