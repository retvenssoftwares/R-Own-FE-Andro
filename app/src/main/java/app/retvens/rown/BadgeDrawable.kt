package app.retvens.rown

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class BadgeDrawable(context: Context, private val count: Int) : Drawable() {

    private val badgePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.badge_background_color)
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = context.resources.getDimension(R.dimen.badge_text_size)
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private val badgeBounds = Rect()
    private var textX = 0f
    private var textY = 0f

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val width = bounds.right - bounds.left
        val height = bounds.bottom - bounds.top

        // Draw the badge background circle
        val radius = (Math.max(width, height) / 2).toFloat()
        canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, radius, badgePaint)

        // Draw the badge count text
        val text = count.toString()
        textPaint.getTextBounds(text, 0, text.length, badgeBounds)
        textX = (width - badgeBounds.width()).toFloat() / 2
        textY = (height + badgeBounds.height()).toFloat() / 2
        canvas.drawText(text, textX, textY, textPaint)
    }

    override fun setAlpha(alpha: Int) {
        // Not needed
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        // Not needed
    }

    override fun getOpacity(): Int {
        return android.graphics.PixelFormat.UNKNOWN
    }
}