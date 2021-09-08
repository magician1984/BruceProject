package idv.bruce.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import idv.bruce.player.databinding.FragmentRtspBinding
import idv.bruce.player.databinding.FragmentVideoBinding

class RTSTFragment : Fragment() {
    private companion object {
        const val PLAY_URL =
            "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
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
        val item: MediaItem = MediaItem.fromUri(PLAY_URL)

        player.setMediaItem(item)

        player.prepare()
    }

    private fun stopVideo() {
        player.stop()
    }
}