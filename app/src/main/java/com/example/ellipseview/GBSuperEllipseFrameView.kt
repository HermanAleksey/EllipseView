package com.example.ellipseview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout


class GBSuperEllipseFrameView @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(ctx, attributeSet, defStyleAttr) {

    private var ellipseColor: Int = Color.BLACK
    private var cornerRounding: Int = 0

    init {
        Log.e("TAGG", ": Init")
        init(attributeSet, defStyleAttr)
        drawSuperEllipse()
        clipToOutline = true
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.GBSuperEllipseView, defStyleAttr, 0
            )
            try {
                ellipseColor = typedArray.getColor(
                    R.styleable.GBSuperEllipseView_shapeColor,
                    Color.GRAY
                )
                cornerRounding =
                    typedArray.getInteger(R.styleable.GBSuperEllipseView_cornerRounding, 0)
            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun drawSuperEllipse() {
        drawSkeleton()
        drawCorners()
    }

    private fun drawSkeleton() {
        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
        )
        val skeletonView = SuperEllipseSkeletonView(context).apply {
            setParams(ellipseColor, cornerRounding)
        }
        addView(skeletonView, layoutParams)
    }

    private fun drawCorners() {
        drawCorner(gravity = Gravity.END or Gravity.TOP, rotation = 0F)
        drawCorner(gravity = Gravity.START or Gravity.TOP, rotation = 270F)
        drawCorner(gravity = Gravity.START or Gravity.BOTTOM, rotation = 180F)
        drawCorner(gravity = Gravity.END or Gravity.BOTTOM, rotation = 90F)
    }

    private fun drawCorner(gravity: Int, rotation: Float) {
        val layoutParams = LayoutParams(
            cornerRounding * 2,
            cornerRounding * 2
        ).apply {
            this.gravity = gravity
        }
        val cornerView = SuperEllipseView(context).apply {
            setParam(ellipseColor)
            this.rotation = rotation
        }

        this.addView(cornerView, layoutParams)
    }
}


