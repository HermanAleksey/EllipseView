package com.example.ellipseview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView

class SuperEllipseCornerView @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(ctx, attributeSet, defStyleAttr) {

    private var ellipseColor: Int = Color.BLACK

    fun setParam(ellipseColor: Int) {
        this.ellipseColor = ellipseColor
        Log.e("TAGG", "setParam: Corners")

        val cornerDrawable =
            AppCompatResources.getDrawable(context, R.drawable.ic_corner_round_super_ellipse)
        setImageDrawable(cornerDrawable)
        setColorFilter(ellipseColor, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    init {
        clipToOutline = true
    }
}