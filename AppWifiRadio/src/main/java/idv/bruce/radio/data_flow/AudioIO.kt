package idv.bruce.radio.data_flow

import android.media.AudioRecord
import android.media.AudioTrack
import java.nio.ByteBuffer

class AudioIO : Pipeline.Module<ByteBuffer>("Audio") {
    private lateinit var audioTrack : AudioTrack

    private lateinit var audioRecorder : AudioRecord

    val inputPipeline:Pipeline<ByteArray> = Pipeline()

    fun init(){

    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override fun process(data : ByteBuffer?) : Boolean {
        TODO("Not yet implemented")
    }


}