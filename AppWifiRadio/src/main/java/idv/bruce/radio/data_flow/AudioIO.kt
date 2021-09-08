package idv.bruce.radio.data_flow

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import java.nio.ByteBuffer

class AudioIO : Pipeline.Module<ByteBuffer>("Audio") {
    private lateinit var audioTrack : AudioTrack

    private lateinit var audioRecorder : AudioRecord

    val recordPipeline : Pipeline<ByteArray> = Pipeline()

    val speakerModule : Pipeline.Module<ByteBuffer> = this

    var simpleRate : Int = 44100

    private var isInit : Boolean = false

    private val recorderChannelFormat : Int = AudioFormat.CHANNEL_IN_MONO

    private val audioFormat : Int = AudioFormat.ENCODING_PCM_8BIT


    fun init() {
        if (isInit) return

        var bufferSize : Int =
            AudioRecord.getMinBufferSize(simpleRate, recorderChannelFormat, audioFormat)

        audioRecorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            simpleRate,
            recorderChannelFormat,
            audioFormat,
            bufferSize
        )

//        audioTrack = AudioTrack()


        isInit = true
    }

    fun startRecord() {
        if (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) return
    }

    fun stopRecord() {

    }

    fun openSpeaker() {
        if (audioRecorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) return
    }

    fun closeSpeaker() {

    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override fun process(data : ByteBuffer?) : Boolean {
        TODO("Not yet implemented")
    }


}