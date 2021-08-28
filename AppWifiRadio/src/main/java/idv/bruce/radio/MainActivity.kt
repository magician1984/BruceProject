package idv.bruce.radio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import idv.bruce.radio.data_flow.DataProcessPipeline
import idv.bruce.radio.data_flow.MulticastIO

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sender : MulticastIO = MulticastIO(this)

        sender.join("228.5.6.7", 8998)

        val processor : DataProcessPipeline.DataProcessor =
            object : DataProcessPipeline.DataProcessor("T"){
                override fun prePass(data : ByteArray) {
                    TODO("Not yet implemented")
                }
            }


        Thread().run {
            while (true) {
                Log.d("Trace", "Put data")

                Thread.sleep(1000)
            }
        }
    }
}