package com.rprihodko.habrareader.common

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.text.style.LineBackgroundSpan

// from https://www.geeksforgeeks.org/how-to-display-html-in-textview-along-with-images-in-android/
class QuoteSpanClass (
    private val stripeColor: Int,
    private val stripeWidth: Float,
    private val gap: Float
) : LeadingMarginSpan {

    // Margin for the block quote tag
    override fun getLeadingMargin(first: Boolean): Int {
        return (stripeWidth + gap).toInt()
    }

    // this function draws the margin.
    override fun drawLeadingMargin(
        c: Canvas,
        p: Paint,
        x: Int,
        dir: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        first: Boolean,
        layout: Layout
    ) {

        val style = p.style
        val paintColor = p.color
        p.style = Paint.Style.FILL
        p.color = stripeColor

        // Creating margin according to color and stripewidth it recieves
        // Press CTRL+Q on function name to read more
        c.drawRect(x.toFloat(), top.toFloat(), x + dir * stripeWidth, bottom.toFloat(), p)
        p.style = style
        p.color = paintColor
    }
}