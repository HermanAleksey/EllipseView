package com.example.ellipseview.ellipse

import android.graphics.*
import com.example.ellipseview.R
import kotlin.math.*

class EllipseImageViewController(
    private val view: EllipseImageView,
) {
    var backgroundColorSource: Int = Color.WHITE
    var cornerRounding: Int = 0
    var sidesWithCorners: List<EllipseImageView.Angle> = listOf()

    fun setMaskToColoredBitmap() {
        //if image expected - have to wait for [setImage] method
        if (view.useImageSource) return

        val contentBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888).apply {
                eraseColor(backgroundColorSource)
            }

        setMaskToOriginSource(contentBitmap)
    }

    fun setImage(drawableRes: Int) {
        if (!view.useImageSource) return

        val contentBitmap = BitmapFactory.decodeResource(view.resources, drawableRes)
        setMaskToOriginSource(contentBitmap)
    }

    private fun setMaskToOriginSource(contentBitmap: Bitmap) {
        //if view size is wrap_content or 0
        val viewWidth = if (view.width <= 1) contentBitmap.width else view.width
        val viewHeight = if (view.height <= 1) contentBitmap.height else view.height

        val maskBitmap = getBitmapMask(viewWidth, viewHeight, cornerRounding)
        val resultBitmap =
            Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888)
        val mCanvas = Canvas(resultBitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }

        mCanvas.apply {
            drawBitmap(contentBitmap, 0F, 0F, null)
            drawBitmap(maskBitmap, 0F, 0F, paint)
        }

        paint.xfermode = null
        view.setImageBitmap(resultBitmap)
        view.setBackgroundResource(R.color.trans)
    }

    private fun getBitmapMask(width: Int, height: Int, ellipseSize: Int): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(bitmap).apply {
            drawTopLeftCorner(ellipseSize, paint)
            drawTopRightCorner(width, ellipseSize, paint)
            drawBottomLeftCorner(ellipseSize, height, paint)
            drawBottomRightCorner(width, ellipseSize, height, paint)
            //horizontal rect
            drawRect(
                0f,
                ellipseSize.toFloat(),
                width.toFloat(),
                (height - ellipseSize).toFloat(),
                paint
            )
            //vertical rect
            drawRect(
                ellipseSize.toFloat(),
                0f,
                (width - ellipseSize).toFloat(),
                height.toFloat(),
                paint
            )
        }
        return bitmap
    }

    private fun Canvas.drawTopLeftCorner(
        ellipseSize: Int,
        paint: Paint,
    ) {
        if (sidesWithCorners.contains(EllipseImageView.Angle.TOP_LEFT))
            drawSuperEllipse(
                offsetLeft = ellipseSize,
                offsetTop = ellipseSize,
                size = ellipseSize,
                paint = paint
            )
        else drawRect(
            0F,
            0F,
            ellipseSize.toFloat(),
            ellipseSize.toFloat(),
            paint
        )
    }

    private fun Canvas.drawTopRightCorner(
        width: Int,
        ellipseSize: Int,
        paint: Paint,
    ) {
        if (sidesWithCorners.contains(EllipseImageView.Angle.TOP_RIGHT))
            drawSuperEllipse(
                offsetLeft = width - ellipseSize,
                offsetTop = ellipseSize,
                size = ellipseSize,
                paint = paint
            )
        else drawRect(
            (width - ellipseSize).toFloat(),
            0F,
            width.toFloat(),
            ellipseSize.toFloat(),
            paint
        )
    }

    private fun Canvas.drawBottomLeftCorner(
        ellipseSize: Int,
        height: Int,
        paint: Paint,
    ) {
        if (sidesWithCorners.contains(EllipseImageView.Angle.BOTTOM_LEFT))
            drawSuperEllipse(
                offsetLeft = ellipseSize,
                offsetTop = height - ellipseSize,
                size = ellipseSize,
                paint = paint
            )
        else drawRect(
            0F,
            (height - ellipseSize).toFloat(),
            ellipseSize.toFloat(),
            height.toFloat(),
            paint
        )
    }

    private fun Canvas.drawBottomRightCorner(
        width: Int,
        ellipseSize: Int,
        height: Int,
        paint: Paint,
    ) {
        if (sidesWithCorners.contains(EllipseImageView.Angle.BOTTOM_RIGHT))
            drawSuperEllipse(
                offsetLeft = width - ellipseSize,
                offsetTop = height - ellipseSize,
                size = ellipseSize,
                paint = paint
            )
        else drawRect(
            (width - ellipseSize).toFloat(),
            (height - ellipseSize).toFloat(),
            width.toFloat(),
            height.toFloat(),
            paint
        )
    }

    //Optimisation: can only draw the corresponding side using the angle values in the loop. (from 0 to 90 etc.)
    private fun Canvas.drawSuperEllipse(offsetLeft: Int, offsetTop: Int, size: Int, paint: Paint) {
        val path = Path()

        //Has to be Double, integer type will break everything
        val n: Double = 5.0
        val na: Double = 2 / n

        fun sgn(number: Double): Int =
            when {
                number > 0 -> 1
                number < 0 -> -1
                else -> 0
            }

        fun calculateX(angle: Double): Float =
            (abs(cos(angle)).pow(na) * size * sgn(cos(angle))).toFloat() + offsetLeft

        fun calculateY(angle: Double): Float =
            (abs(sin(angle)).pow(na) * size * sgn(sin(angle))).toFloat() + offsetTop

        path.moveTo(calculateX(0.0), calculateY(0.0))
        var i = 0.0
        while (i < PI * 2) {
            path.lineTo(calculateX(i), calculateY(i))
            i += 0.1
        }

        this.drawPath(path, paint)
    }
}