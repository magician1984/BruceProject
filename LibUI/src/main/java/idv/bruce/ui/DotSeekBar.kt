package idv.bruce.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class DotSeekBar(context : Context, attrs : AttributeSet) : View(context, attrs) {
    private var mTitle : String? = null

    private var mTitleColor : Int = 0

    private var mTitleSize : Int = 16

    private var mTickSize : Int = 16

    private var mTickMargin : Int = 8

    private var mTickColor : Int = 0

    private var mTickInterval : Int = 1

    private var mMinValue : Int = 0

    private var mMaxValue : Int = 100

    private var mStartValue : Int = 0

    private var mEnable : Boolean = true

    private var mShowTitle : Boolean = true

    private var mProgress : Int = 0

    init {
        attrs.let {

            val typedArray : TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.DotSeekBar)

            val count : Int = typedArray.indexCount

            var attr : Int

            if(count > 0) {
                for (index in 0 until count) {
                    attr = typedArray.getIndex(index)
                    when (attr) {
                        R.styleable.DotSeekBar_dsb_title -> {
                            mTitle = typedArray.getString(attr)
                        }
                        R.styleable.DotSeekBar_dsb_titleColor -> {
                            mTitleColor = typedArray.getColor(attr, mTitleColor)
                        }
                        R.styleable.DotSeekBar_dsb_titleSize -> {
                            mTickSize = typedArray.getDimensionPixelSize(attr, mTitleSize)
                        }
                    }
                }
            }
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec : Int, heightMeasureSpec : Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas : Canvas?) {
        super.onDraw(canvas)
    }
}