package idv.bruce.player


import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.ConcatenatingMediaSource

class KTVPlayer(context : Context) {
    interface EventListener {
        fun onMTVPlay(item : MTVItem)
        fun onAdPlay(item : MTVItem)
        fun onNextMtvPrepare(item : MTVItem)
        fun onPause()
        fun onResume()
        fun onReplay()
        fun onCut()
        fun onStop()
        fun onListComplete()
        fun onAudioChanged()
    }

    abstract class Item(var uri : Uri) {
        internal abstract fun createMediaItem() : MediaItem
    }

    class MTVItem(var id : Int, var name : String, uri : Uri) : Item(uri) {
        override fun createMediaItem() : MediaItem {
            TODO("Not yet implemented")
        }
    }

    class AdsItem(uri : Uri) : Item(uri) {
        override fun createMediaItem() : MediaItem {
            TODO("Not yet implemented")
        }
    }

    private val player : SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()

    private val adsQueue : ArrayList<AdsItem> = ArrayList()

    private val mtvQueue : ArrayList<MTVItem> = ArrayList()

    var minIdleTime : Long = -1

    val adsList : List<AdsItem>
        get() = adsQueue.toList()

    val mtvList : List<MTVItem>
        get() = mtvQueue.toList()

    fun addMtvItem(index : Int, item : MTVItem) {
        when {
            index == -1 || index >= mtvQueue.size -> mtvQueue.add(item)
            else -> mtvQueue.add(index, item)
        }

    }

    fun addMtvItem(item : MTVItem) = addMtvItem(-1, item)

    fun addMtvItems(index : Int, vararg items : MTVItem) {
        when {
            index == -1 || index >= mtvQueue.size -> mtvQueue.addAll(items)
            else -> mtvQueue.addAll(index, items.toList())
        }
    }

    fun addMtvItems(vararg items : MTVItem) = mtvQueue.addAll(items.toList())

    fun deleteMtvItem(index : Int) : Boolean {
        return if (index >= mtvQueue.size)
            false
        else {
            mtvQueue.removeAt(index)
            true
        }
    }

    fun addAdsItem(item : AdsItem) {
        val dataSrc: ConcatenatingMediaSource = ConcatenatingMediaSource()

    }

    fun addAdsItems(vararg items : AdsItem) {
        for (item in items) {
            addAdsItem(item)
        }
    }

    fun release() {
        player.release()
    }

    private val analyticsListener:AnalyticsListener = object :AnalyticsListener{

    }
}