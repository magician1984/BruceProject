package idv.bruce.camera.ui_com

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class MaskView(context : Context, attributeSet : AttributeSet) : View(context, attributeSet, 0) {
    var area : Rect? = null

    private val strokePaint : Paint = Paint()

    private val maskPaint:Paint = Paint()

    init {
        strokePaint.apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }



        maskPaint.apply {
            color = Color.WHITE
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            flags = Paint.ANTI_ALIAS_FLAG
        }
    }

    override fun onDraw(canvas : Canvas?) {
        super.onDraw(canvas)

        if (area == null) {
            val size : Int = (width.coerceAtMost(height) * 0.95).toInt()

            area = Rect(
                (width - size) / 2,
                (height - size) / 2,
                (width + size) / 2,
                (height + size) / 2
            )
        }

        canvas?.drawColor(Color.parseColor("#80000000"))
        canvas?.drawRect(area!!, maskPaint)
        canvas?.drawRect(area!!, strokePaint)
    }
}