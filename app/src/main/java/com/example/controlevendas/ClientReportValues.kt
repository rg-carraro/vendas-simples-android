package com.example.controlevendas

import android.content.Context
import android.graphics.Typeface
import android.text.Layout
import android.view.View
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import kotlin.math.ceil

/** Apenas apresentação: recebe os três valores já calculados e formatados. */
class ClientReportValues(
    context: Context, sold: String, received: String, remaining: String,
    textColor: Int, labelColor: Int
) : LinearLayout(context) {
    private val gap = dp(6)
    private var columns = 0
    private val cells = listOf("Vendido" to sold, "Recebido" to received, "Faltante" to remaining)
        .mapIndexed { index, (label, value) ->
            LinearLayout(context).apply {
                orientation = VERTICAL
                setPadding(dp(2), dp(2), dp(2), dp(2))
                addView(TextView(context).apply {
                    text = label
                    textSize = 11f
                    setTextColor(labelColor)
                })
                addView(TextView(context).apply {
                    text = value
                    textSize = 14f
                    setTextColor(textColor)
                    typeface = if (index == 2) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                })
            }
        }

    init { orientation = VERTICAL }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val available = (View.MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight).coerceAtLeast(0)
        val required = cells.maxOf { cell ->
            ceil((0 until cell.childCount).maxOf {
                val text = cell.getChildAt(it) as TextView
                Layout.getDesiredWidth(text.text, text.paint).toDouble()
            }).toInt() + cell.paddingLeft + cell.paddingRight
        }.coerceAtLeast(dp(80))
        val fontLimit = if (resources.configuration.fontScale > 1.3f) {
            if (available >= dp(560)) 2 else 1
        } else 3
        val chosen = ((available + gap) / (required + gap)).coerceIn(1, fontLimit)
        if (chosen != columns) {
            cells.forEach { (it.parent as? LinearLayout)?.removeView(it) }
            removeAllViews()
            cells.chunked(chosen).forEach { group ->
                val row = LinearLayout(context).apply { orientation = HORIZONTAL }
                repeat(chosen) { index ->
                    row.addView(group.getOrNull(index) ?: Space(context), LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f).apply {
                        if (index > 0) leftMargin = gap
                    })
                }
                addView(row, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            }
            columns = chosen
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}
