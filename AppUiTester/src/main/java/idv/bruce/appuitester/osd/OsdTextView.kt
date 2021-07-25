package idv.bruce.appuitester.osd

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class OsdTextView(context : Context, attributeSet : AttributeSet) :
    AppCompatTextView(context, attributeSet, 0) {

    private val timer : ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    var duration : Long = -1L

    fun show() {
        visibility = VISIBLE

        if (duration != -1L)
            timer.schedule({
                post {
                    visibility = INVISIBLE
                }
            }, duration, TimeUnit.MILLISECONDS)
    }
}