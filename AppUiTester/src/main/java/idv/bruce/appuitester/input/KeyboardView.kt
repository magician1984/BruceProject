package idv.bruce.appuitester.input

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.inputmethodservice.KeyboardView
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import kotlin.math.pow
import kotlin.math.sqrt

class KeyboardView @JvmOverloads constructor(
    context : Context,
    attrs : AttributeSet,
    defStyleAttr : Int = android.R.style.Theme_DeviceDefault,
    defStyleRes : Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val TAG = "KeyboardView"
    }

    interface OnKeyboardActionListener {
        fun onKeyPressed(text : CharSequence)
    }

    var keyboard : Keyboard? = null

    private var mBuffer : Bitmap? = null

    private var keys : ArrayList<Keyboard.Key>? = null

    private var keyboardRect : Rect? = null

    private val gestureDetector : GestureDetectorCompat =
        GestureDetectorCompat(context, object : GestureDetector.OnGestureListener {
            override fun onDown(e : MotionEvent?) : Boolean {
                return true
            }

            override fun onShowPress(e : MotionEvent?) {

            }

            override fun onSingleTapUp(e : MotionEvent?) : Boolean {
                Log.d(TAG, "onSingleTapUp : ${e?.x}, ${e?.y}")
                return true
            }

            override fun onScroll(
                e1 : MotionEvent?,
                e2 : MotionEvent?,
                distanceX : Float,
                distanceY : Float
            ) : Boolean {
                return true
            }

            override fun onLongPress(e : MotionEvent?) {

            }

            override fun onFling(
                e1 : MotionEvent?,
                e2 : MotionEvent?,
                velocityX : Float,
                velocityY : Float
            ) : Boolean {

                return true
            }
        }).apply {
            setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
                override fun onSingleTapConfirmed(e : MotionEvent?) : Boolean {
                    Log.d(TAG, "onSingleTapConfirmed : $e")
                    return true
                }

                override fun onDoubleTap(e : MotionEvent?) : Boolean {
                    Log.d(TAG, "onDoubleTap : $e")
                    return true
                }

                override fun onDoubleTapEvent(e : MotionEvent?) : Boolean {
                    Log.d(TAG, "onDoubleTapEvent : $e")
                    return true
                }
            })
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event : MotionEvent?) : Boolean {
        return gestureDetector.onTouchEvent(event)
    }


    override fun onDraw(canvas : Canvas?) {
        super.onDraw(canvas)
        if (mBuffer == null && keyboard != null)
            drawBuffer()

        if (keyboard != null)
            canvas?.drawBitmap(mBuffer!!, paddingStart.toFloat(), paddingTop.toFloat(), null)
    }

    private fun drawBuffer() {
        val board : Keyboard = keyboard ?: return

        keyboardRect = Rect(paddingStart, paddingTop, width - paddingEnd, height - paddingBottom)

        keys = board.getKeySets(Size(keyboardRect!!.width(), keyboardRect!!.height()))

        mBuffer = Bitmap.createBitmap(keyboardRect!!.width(), keyboardRect!!.height(), Bitmap.Config.ARGB_8888)

        val mCanvas : Canvas = Canvas(mBuffer!!)

        val keySize : Int = keyboard?.keyRealSize ?: return

        val mPaint : Paint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
        }

        val mTextPaint : TextPaint = TextPaint(mPaint).apply {
            textSize = sqrt(2.0 * (keySize * 3 / 5).toDouble().pow(2.0)).toFloat()
            style = Paint.Style.FILL
        }


        val x : Float = (keySize.toFloat() / 2.0f) - (mTextPaint.textSize / 2.0f)

        val y : Float = (keySize.toFloat() / 2.0f) + (mTextPaint.textSize / 2.0f)

        for (key in keys!!) {
            Log.d(TAG, "Key : ${key.label}, (${key.point.x},${key.point.y})")
            mCanvas.save()
            mCanvas.translate(key.point.x.toFloat(), key.point.y.toFloat())
            mCanvas.drawRect(
                0f,
                0f,
                key.size.width.toFloat(),
                key.size.height.toFloat(),
                mPaint
            )
            mCanvas.drawText(key.label.toString(),  x, y, mTextPaint)
            mCanvas.restore()
        }
    }
}