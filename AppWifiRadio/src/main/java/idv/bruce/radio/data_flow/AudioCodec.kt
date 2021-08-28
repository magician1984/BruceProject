package idv.bruce.radio.data_flow

import java.nio.ByteBuffer

class AudioCodec {
    val encoder : Pipeline.Module<ByteBuffer> =
        object : Pipeline.Module<ByteBuffer>("AudioEncoder") {
            override fun release() {
                TODO("Not yet implemented")
            }

            override fun process(data : ByteBuffer?) : Boolean {
                TODO("Not yet implemented")
            }
        }

    val decoder : Pipeline.Module<ByteBuffer> =
        object : Pipeline.Module<ByteBuffer>("AudioDecoder") {
            override fun release() {
                TODO("Not yet implemented")
            }

            override fun process(data : ByteBuffer?) : Boolean {
                TODO("Not yet implemented")
            }
        }
}