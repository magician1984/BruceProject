package idv.bruce.radio.net

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import java.lang.Exception
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MulticastIO(context : Context) {
    private var socket : MulticastSocket? = null

    private var group : InetAddress? = null

    private val queue : Queue<ByteArray> = LinkedList()

    private val wifiManager : WifiManager =
        context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val senderService : ExecutorService =
        Executors.newSingleThreadExecutor { Thread(it, "radio_sender") }

    private val receiverService : ExecutorService =
        Executors.newSingleThreadExecutor { Thread(it, "radio_receiver") }

    private val handlerService : ExecutorService =
        Executors.newSingleThreadExecutor { Thread(it, "radio_handler") }

    val pipelineOutEndpoint : DataProcessPipeline = object : DataProcessPipeline("multicast") {
        override fun process(data : ByteArray) {
            queue.offer(data)
        }
    }

    val pipelineInEndPoint : DataProcessPipeline = object : DataProcessPipeline("multicast") {
        override fun process(data : ByteArray) {

        }
    }

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


    private val sendRunnable : Runnable = Runnable {
        val packet : DatagramPacket = DatagramPacket(ByteArray(1), 1)

        while (true) {
            val data : ByteArray = queue.poll() ?: continue

            packet.data = data

            Log.d("Trace", "Send data : ${String(data)}")
            socket?.send(packet) ?: break
        }
    }

    private val recvRunnable : Runnable = Runnable {

    }
}