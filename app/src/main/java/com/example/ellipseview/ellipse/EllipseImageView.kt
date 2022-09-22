package com.example.ellipseview.ellipse

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.example.ellipseview.R


class EllipseImageView @JvmOverloads constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(ctx, attributeSet, defStyleAttr) {

    private val controller = EllipseImageViewController(this)

    enum class Angle(private val index: Int) {
        TOP_LEFT(0),
        TOP_RIGHT(1),
        BOTTOM_RIGHT(2),
        BOTTOM_LEFT(3);

        fun parseFromString(str: String): List<Angle> {
            val list = mutableListOf<Angle>()
            values().forEach {
                if (str.contains(it.index.toString()))
                    list.add(it)
            }
            return list
        }
    }

    var useImageSource: Boolean = false

    init {
        init(attributeSet, defStyleAttr)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.SuperEllipseView, defStyleAttr, 0
            )
            try {
                useImageSource = typedArray.getBoolean(
                    R.styleable.SuperEllipseView_useImageSource,
                    false
                )
                val cornersWithAngleString = typedArray.getString(
                    R.styleable.SuperEllipseView_sidesWithCorners
                ) ?: ""
                controller.sidesWithCorners = Angle.TOP_LEFT.parseFromString(cornersWithAngleString)

                if (!useImageSource) {
                    controller.backgroundColorSource = typedArray.getColor(
                        R.styleable.SuperEllipseView_backgroundColorSource,
                        Color.GRAY
                    )
                }
                controller.cornerRounding =
                    typedArray.getInteger(R.styleable.SuperEllipseView_ellipseValue, 0)

            } finally {
                typedArray.recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setMaskToColoredBitmap()
    }

    private fun setMaskToColoredBitmap() {
        controller.setMaskToColoredBitmap()
    }

    fun setImage(@DrawableRes drawableRes: Int) {
        controller.setImage(drawableRes)
    }

}