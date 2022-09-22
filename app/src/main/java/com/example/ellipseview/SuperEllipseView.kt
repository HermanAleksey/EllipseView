package com.example.ellipseview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.*


class SuperEllipseView @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(ctx, attributeSet, defStyleAttr) {

    private val path = Path()
    private val pathPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ellipseColor
        }
    }

    private var ellipseColor: Int = Color.BLACK

    fun setParam(ellipseColor: Int) {
//        this.ellipseColor = ellipseColor

        requestLayout()
    }

    init {
        clipToOutline = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSuperEllipse(canvas)
    }

    private fun drawSuperEllipse(canvas: Canvas?) {
        val centralOffsetX = width / 2
        val centralOffsetY = height / 2
        val mWidth = width / 2
        val mHeight = height / 2

        //Has to be double, integer type will break everything
        val n: Double = 5.0
        val na: Double = 2 / n

        fun calculateX(angle: Double): Float =
            (abs(cos(angle)).pow(na) * mWidth * sgn(cos(angle))).toFloat() + centralOffsetX

        fun calculateY(angle: Double): Float =
            (abs(sin(angle)).pow(na) * mHeight * sgn(sin(angle))).toFloat() + centralOffsetY

        path.moveTo(calculateX(0.0), calculateY(0.0))
        var i = 0.0
        while (i < PI * 2) {
            path.lineTo(calculateX(i), calculateY(i))
            i += 0.1
        }

        canvas?.drawPath(path, pathPaint)
    }

    private fun sgn(number: Double): Int =
        when {
            number > 0 -> 1
            number < 0 -> -1
            else -> 0
        }
}
