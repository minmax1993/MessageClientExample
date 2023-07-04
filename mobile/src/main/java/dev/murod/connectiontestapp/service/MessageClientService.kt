package dev.murod.connectiontestapp.service

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable

class MessageClientService(private val context: Context) {

    private var listener: MessageClientListener? = null
    private var nodeId: String? = null
    private var connectivityState: MessageClientConnectivity =
        MessageClientConnectivity.NoPairedDevice

    // Inside your Android app
    private var messageClient: MessageClient? = null

    fun prepareConnection() {
        val nodeClient = Wearable.getNodeClient(context)
        messageClient = Wearable.getMessageClient(context)
        val nodeClientTask = nodeClient.connectedNodes

        nodeClientTask.addOnSuccessListener { nodes ->
            // Nodes retrieved successfully
            for (node in nodes) {
                val nodeId = node.id
                this.nodeId = nodeId

                this.connectivityState = MessageClientConnectivity.FoundPairedDevice(nodeId)
                listener?.onClientConnectivity(connectivityState)
            }
        }.addOnFailureListener { exception ->
            // Failed to retrieve nodes
            this.connectivityState = MessageClientConnectivity.NoPairedDevice
            listener?.onClientConnectivity(connectivityState)
        }
    }

    fun sendMessage(msg: String) {
        nodeId?.let {
            Log.e("ZZZZ", "NodeId: ${it}")
            connectTo(it, msg)
        }
    }

    private fun connectTo(nodeId: String, msg: String) {
        // Inside your Android app
        val sendMessageTask = messageClient?.sendMessage(nodeId, msg, null)

        sendMessageTask?.addOnSuccessListener {
            listener?.onMessageSent()
        }?.addOnFailureListener { exception ->
            // Connection failed
            connectivityState =
                MessageClientConnectivity.ConnectionFailed(nodeId = nodeId, msg = exception.message)
            listener?.onClientConnectivity(connectivityState)
        }
    }

    fun addListener(listener: MessageClientListener) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    fun getConnectivityState() = connectivityState
}

sealed class MessageClientConnectivity {
    object NoPairedDevice : MessageClientConnectivity()
    data class ConnectionFailed(val msg: String?, val nodeId: String) : MessageClientConnectivity()
    data class FoundPairedDevice(val nodeId: String) : MessageClientConnectivity()
}

interface MessageClientListener {
    fun onClientConnectivity(state: MessageClientConnectivity)
    fun onMessageSent()
}