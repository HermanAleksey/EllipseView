package com.example.ellipseview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class SuperEllipseSkeletonView @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(ctx, attributeSet, defStyleAttr) {

    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ellipseColor
        }
    }

    private var ellipseColor: Int = Color.BLACK
    private var cornerRounding: Int = 0

    fun setParams(ellipseColor: Int, cornerRounding: Int) {
        this.ellipseColor = ellipseColor
        this.cornerRounding = cornerRounding

        requestLayout()
    }

    override fun onDraw(canvas: Canvas?) {
        Log.e("TAGG", "onDraw: draw skeleton")
        super.onDraw(canvas)
        drawSuperEllipseSkeleton(canvas)
    }

    private fun drawSuperEllipseSkeleton(canvas: Canvas?) {
        val offsetForCorner = cornerRounding.toFloat()
        Log.e("TAGG", "drawSuperEllipseSkeleton: offsetForCorner:$offsetForCorner")
        canvas?.drawRect(offsetForCorner, 0F, width - offsetForCorner, height.toFloat(), paint)
        canvas?.drawRect(0F, offsetForCorner, width.toFloat(), height - offsetForCorner, paint)
    }
}