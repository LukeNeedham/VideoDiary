package com.lukeneedham.videodiary.data.persistence.export


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.BitmapOverlay
import androidx.media3.effect.OverlaySettings

@UnstableApi
class DateOverlay(
    private val text: String,
    private val paddingBottom: Int,
    private val overlaySettings: OverlaySettings
) : BitmapOverlay() {
    private val paintTextSize = 70
    private val paintStrokeWidth = 15

    private val fillPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        textSize = paintTextSize.toFloat()
        isAntiAlias = true
    }
    private val borderPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        textSize = paintTextSize.toFloat()
        strokeWidth = paintStrokeWidth.toFloat()
    }
    private val textBounds = Rect()

    override fun getBitmap(
        presentationTimeUs: Long,
    ): Bitmap {
        val halfStroke = paintStrokeWidth / 2

        fillPaint.getTextBounds(text, 0, text.length, textBounds)
        // Text bounds don't include stroke - we have to manually add it in
        textBounds.set(
            textBounds.left - halfStroke,
            textBounds.top - halfStroke,
            textBounds.right + halfStroke,
            textBounds.bottom + halfStroke,
        )
        val textWidth = textBounds.width()
        val textHeight = textBounds.height()

        val bitmap =
            Bitmap.createBitmap(textWidth, textHeight + paddingBottom, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val x = -textBounds.left.toFloat()
        val y = -textBounds.top.toFloat()

        canvas.drawText(text, x, y, borderPaint)
        canvas.drawText(text, x, y, fillPaint)

        return bitmap
    }

    override fun getOverlaySettings(presentationTimeUs: Long): OverlaySettings = overlaySettings
}