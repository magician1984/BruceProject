package idv.bruce.appuitester.input

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.content.res.XmlResourceParser
import android.graphics.Point
import android.graphics.Rect
import android.print.PrintAttributes
import android.util.DisplayMetrics
import android.util.Size
import android.util.TypedValue
import android.util.Xml
import android.view.Gravity
import android.widget.TextView
import idv.bruce.appuitester.R
import kotlin.math.roundToInt

class Keyboard(context : Context, xmlRes : Int) {
    companion object {
        const val LOCATION_NOT_DEFINED = -1
        const val CODE_NOT_A_KEY = -1
        
        private const val TAG_KEYBOARD = "Keyboard"
        private const val TAG_ROW = "Row"
        private const val TAG_KEY = "Key"

        object Gravity {
            const val START : Int = 0
            const val CENTER : Int = 1
            const val END : Int = 2
        }
    }


    var drawArea : Size? = null

    var isStateChanged : Boolean = false

    var keyMaxWidth : Int = -1

    var keyMaxHeight : Int = -1

    var keyRealSize : Int = -1

    var keyHorizontalGap : Int = 0

    var keyVerticalGap : Int = 0

    var width : Int = 0

    var height : Int = 0


    private val rows : ArrayList<Row> = ArrayList()

    private var maxKeyCountOfRow : Int = 0

    init {
        val metrics : DisplayMetrics = context.resources.displayMetrics

        keyMaxWidth = (48f * metrics.density).toInt()

        keyMaxHeight = keyMaxWidth

        loadKeyboardFromResource(context.resources, xmlRes)
    }

    fun detectKey(x : Int, y : Int) : Key? {
        return null
    }

    fun getKeySets(size : Size) : ArrayList<Key> {
        val keys : ArrayList<Key> = ArrayList()

        val pWidth : Int = size.width
        val pHeight : Int = size.height

        val keyWidth : Int = keyMaxWidth.coerceAtMost(pWidth / maxKeyCountOfRow)
        val keyHeight : Int = keyMaxHeight.coerceAtMost(pHeight / rows.size)

        keyRealSize = keyWidth.coerceAtMost(keyHeight)

        width = keyRealSize * maxKeyCountOfRow

        height = keyRealSize * rows.size

        var cRow : Row
        var cKey : Key

        var rowWidth : Int = 0

        for (i in 0 until rows.size) {
            cRow = rows[i]

            rowWidth = cRow.keys.size * keyRealSize

            cRow.x = when (cRow.gravity) {
                Gravity.START -> 0
                Gravity.CENTER -> (pWidth / 2) - (rowWidth / 2)
                Gravity.END -> pWidth - rowWidth
                else -> 0
            }

            cRow.y = i * keyRealSize
            for (j in 0 until cRow.keys.size) {
                cKey = cRow.keys[j]
                cKey.areaRect.set(
                    cRow.x + (j * keyRealSize),
                    cRow.y,
                    cRow.x + ((j + 1) * keyRealSize),
                    cRow.y + keyRealSize
                )
                keys.add(cKey)
            }
        }

        return keys
    }

    private fun loadKeyboardFromResource(res : Resources, xmlRes : Int) {
        val parser : XmlResourceParser = res.getXml(xmlRes)

        var event : Int
        var row : Row? = null
        var key : Key? = null

        rows.clear()
        maxKeyCountOfRow = 0

        while (parser.next().also { event = it } != XmlResourceParser.END_DOCUMENT) {

            if (event == XmlResourceParser.START_TAG) {
                when (parser.name) {
                    TAG_ROW -> {
                        row = Row(res, parser)
                    }
                    TAG_KEY -> {
                        key = Key(res, parser)
                    }
                    TAG_KEYBOARD -> {

                    }
                }
            } else if (event == XmlResourceParser.END_TAG) {
                when (parser.name) {
                    TAG_ROW -> {
                        rows.add(row ?: continue)
                        maxKeyCountOfRow = maxKeyCountOfRow.coerceAtLeast(row.keys.size)
                    }
                    TAG_KEY -> {
                        row?.keys?.add(key ?: continue)
                    }
                }
            }
        }
    }

    private fun parseKeyboardAttr(res : Resources, parser : XmlResourceParser) {
        val typedArray : TypedArray =
            res.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.Keyboard)

        typedArray.recycle()
    }

    class Row internal constructor(res : Resources, parser : XmlResourceParser) {
        val keys : ArrayList<Key> = ArrayList()

        var x : Int = LOCATION_NOT_DEFINED

        var y : Int = LOCATION_NOT_DEFINED

        var gravity : Int = Gravity.CENTER

        init{
            val typedArray:TypedArray = res.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.Keyboard_Row)

            gravity = typedArray.getInt(R.styleable.Keyboard_Row_gravity, Gravity.CENTER)

            typedArray.recycle()
        }

    }

    inner class Key internal constructor(res : Resources, parser : XmlResourceParser) {
        var label : CharSequence = ""
        var code : Int = CODE_NOT_A_KEY
        var text : CharSequence? = null
        internal var areaRect : Rect = Rect(0, 0, 0, 0)
        internal var paddingRect : Rect = Rect(0, 0, 0, 0)
        internal var marginRect : Rect = Rect(0, 0, 0, 0)
        var isPressed : Boolean = false
        var isEnable : Boolean = true

        val point : Point
            get() = Point(areaRect.left, areaRect.right)


        val size : Size
            get() {
                return Size(areaRect.width(), areaRect.height())
            }

        init {
            val typedArray : TypedArray =
                res.obtainAttributes(Xml.asAttributeSet(parser), R.styleable.Keyboard_Key)
            label = typedArray.getText(R.styleable.Keyboard_Key_keyLabel)
            text = typedArray.getText(R.styleable.Keyboard_Key_keyOutputText)
            code = typedArray.getInt(R.styleable.Keyboard_Key_codes, CODE_NOT_A_KEY)
            typedArray.recycle()
        }
    }

}