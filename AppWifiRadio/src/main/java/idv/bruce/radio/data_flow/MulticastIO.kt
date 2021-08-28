package idv.bruce.radio.data_flow

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import java.lang.Exception
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MulticastIO(context : Context) : Pipeline.Module<ByteBuffer>("Multicast") {
    private var socket : MulticastSocket? = null

    private var group : InetAddress? = null

    private val sendQueue : Queue<ByteArray> = LinkedList()

    private val wifiManager : WifiManager =
        context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val senderService : ExecutorService =
        Executors.newSingleThreadExecutor { Thread(it, "radio_sender") }

    private val receiverService : ExecutorService =
        Executors.newSingleThreadExecutor { Thread(it, "radio_receiver") }

    private val handlerService : ExecutorService =
        Executors.newSingleThreadExecutor { Thread(it, "radio_handler") }

    val inputPipeline : Pipeline<ByteBuffer> = Pipeline()

    fun join(address : String, port : Int) : Boolean {
        quit()

        socket = MulticastSocket(port).apply {
            loopbackMode = true
        }

        val group : InetAddress = InetAddress.getByName(address)

        if (!group.isMulticastAddress) return false

        try {
            socket!!.joinGroup(group)

            senderService.submit(sendRunnable)
        } catch (e : Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun quit() {
        socket?.leaveGroup(group ?: return)

        socket = null

        group = null
    }


    override fun process(data : ByteBuffer?) : Boolean {
        val buffer : ByteBuffer = data ?: return false

        buffer.flip()

        val payload : ByteArray = ByteArray(buffer.remaining())

        buffer.get(payload)

        return sendQueue.offer(payload)
    }

    override fun release() {

    }

    private val sendRunnable : Runnable = Runnable {
        val packet : DatagramPacket = DatagramPacket(ByteArray(1), 1)

        while (true) {
            val data : ByteArray = sendQueue.poll() ?: continue

            packet.data = data

            Log.d("Trace", "Send data : ${String(data)}")
            socket?.send(packet) ?: break
        }
    }

    private val recvRunnable : Runnable = Runnable {

    }
}