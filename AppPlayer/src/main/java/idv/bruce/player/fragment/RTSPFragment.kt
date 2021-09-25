package idv.bruce.player.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.LoadEventInfo
import com.google.android.exoplayer2.source.MediaLoadData
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.util.MimeTypes
import idv.bruce.player.databinding.FragmentRtspBinding
import java.io.IOException

class RTSPFragment : Fragment() {
    private companion object {
        const val PLAY_URL =
            "https://www.google.com/search?q=rtsp%3A%2F%2Fdemo%3Ademo%40ipvmdemo.dyndns.org%3A5541%2Fonvif-media%2Fmedia.amp%3Fprofile%3Dprofile_1_h264%26sessiontimeout%3D60%26streamtype%3Dunicast&rlz=1C1VDKB_zh-TWTW952TW952&oq=rtsp%3A%2F%2Fdemo%3Ademo%40ipvmdemo.dyndns.org%3A5541%2Fonvif-media%2Fmedia.amp%3Fprofile%3Dprofile_1_h264%26sessiontimeout%3D60%26streamtype%3Dunicast&aqs=chrome..69i57j69i58.1019j0j9&sourceid=chrome&ie=UTF-8"
    }

    private lateinit var binding : FragmentRtspBinding

    private lateinit var player : SimpleExoPlayer

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentRtspBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        initPlayer()
    }

    override fun onResume() {
        super.onResume()
        startVideo()
    }

    override fun onPause() {
        super.onPause()
        stopVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun initPlayer() {


        player = SimpleExoPlayer.Builder(requireContext()).build()

        player.playWhenReady = true

        binding.display.player = player
    }

    private fun releasePlayer() {
        player.release()
    }

    private fun startVideo() {
        val item: MediaItem = MediaItem.Builder()
            .setMimeType(MimeTypes.APPLICATION_RTSP)
            .setUri(PLAY_URL)
            .build()


        val mediaSource: RtspMediaSource = RtspMediaSource.Factory()
            .createMediaSource(item)

        player.setMediaSource(mediaSource)

        player.addAnalyticsListener(object :AnalyticsListener{
            override fun onPlayerError(
                eventTime : AnalyticsListener.EventTime,
                error : PlaybackException
            ) {
                super.onPlayerError(eventTime, error)
                Log.e("Trace", "${error.message}")
            }

            override fun onLoadError(
                eventTime : AnalyticsListener.EventTime,
                loadEventInfo : LoadEventInfo,
                mediaLoadData : MediaLoadData,
                error : IOException,
                wasCanceled : Boolean
            ) {
                super.onLoadError(eventTime, loadEventInfo, mediaLoadData, error, wasCanceled)
                Log.e("Trace", "${error.message}")
            }
        })
        player.prepare()
    }

    private fun stopVideo() {
        player.stop()
    }
}