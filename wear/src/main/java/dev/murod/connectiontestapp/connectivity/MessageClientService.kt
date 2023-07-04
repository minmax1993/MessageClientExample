package dev.murod.connectiontestapp.connectivity

import android.content.Context
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageClient.OnMessageReceivedListener
import com.google.android.gms.wearable.Wearable

class MessageClientService(context: Context) {

    private var msgClient: MessageClient = Wearable.getMessageClient(context)

    fun addListener(listener: OnMessageReceivedListener) {
        msgClient.addListener(listener)
    }

    // Inside your Android app
//        val messageClient = Wearable.getMessageClient(this)
//        val sendMessageTask = messageClient.sendMessage("nodeId", "/path/to/message", null)
//
//        sendMessageTask.addOnSuccessListener {
//            // Connection successful
//            Log.e("ZZZ", "Connection successful")
//        }.addOnFailureListener { exception ->
//            // Connection failed
//            Log.e("ZZZ", "Connection failed: ${exception.message}")
//        }
}