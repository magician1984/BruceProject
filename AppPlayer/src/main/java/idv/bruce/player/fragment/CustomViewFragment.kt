package idv.bruce.player.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.video.VideoSize
import idv.bruce.player.databinding.FragmentCustomViewBinding

class CustomViewFragment : Fragment() {
    private companion object {
        const val PLAY_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    }

    private lateinit var binding : FragmentCustomViewBinding

    private lateinit var player : SimpleExoPlayer

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentCustomViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        binding.display.setEGLContextClientVersion(2)

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

        player.setVideoSurfaceView(binding.display)

        player.addAnalyticsListener(object : AnalyticsListener{
            override fun onVideoSizeChanged(
                eventTime : AnalyticsListener.EventTime,
                videoSize : VideoSize
            ) {
                Log.d("Trace", "Video size : ${videoSize.width}, ${videoSize.height}")
            }
        })
    }

    private fun releasePlayer() {
        player.release()
    }

    private fun startVideo() {
        val item : MediaItem = MediaItem.fromUri(PLAY_URL)

        player.setMediaItem(item)

        player.prepare()
    }

    private fun stopVideo() {
        player.stop()
    }
}