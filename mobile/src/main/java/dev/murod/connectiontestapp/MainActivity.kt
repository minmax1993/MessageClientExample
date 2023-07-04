package dev.murod.connectiontestapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.Wearable
import dev.murod.connectiontestapp.di.Injection
import dev.murod.connectiontestapp.service.MessageClientConnectivity

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModels(
        factoryProducer = { MainViewModel.Factory(Injection.msgClientService) }
    )

    private var nodeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_send).setOnClickListener {
            vm.sentMessage()
        }

        lifecycleScope.launchWhenStarted {
            vm.connectivityState.collect {
                findViewById<TextView>(R.id.txt_view).text = when (it) {
                    is MessageClientConnectivity.ConnectionFailed -> {
                        "Connection Failed: ${nodeId}"
                    }

                    is MessageClientConnectivity.NoPairedDevice -> {
                        "No Paired device"
                    }

                    is MessageClientConnectivity.FoundPairedDevice -> {
                        "Connected: ${it.nodeId}"
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            vm.msgSentState.collect {
                findViewById<TextView>(R.id.txt_msg_view).text =
                    if (it) "Message Sent" else "Sending"
            }
        }

//        // Inside your Android app
//        val nodeClient = Wearable.getNodeClient(this)
//        val nodeClientTask = nodeClient.connectedNodes
//
//        nodeClientTask.addOnSuccessListener { nodes ->
//            // Nodes retrieved successfully
//            for (node in nodes) {
//                val nodeId = node.id
//                this.nodeId = nodeId
//                connectTo(nodeId, "/path/to/message")
//            }
//        }.addOnFailureListener { exception ->
//            // Failed to retrieve nodes
//            findViewById<TextView>(R.id.txt_view).text = "Node id not found: ${exception.message}"
//        }
//
//        findViewById<Button>(R.id.btn_send).setOnClickListener {
//            nodeId?.let {
//                connectTo(it, "/path/to/message ${UUID.randomUUID()}")
//            }
//        }
    }

    private fun connectTo(nodeId: String, msg: String) {
        // Inside your Android app
        val messageClient = Wearable.getMessageClient(this)
        val sendMessageTask = messageClient.sendMessage(nodeId, msg, null)


        sendMessageTask.addOnSuccessListener {
            // Connection successful
            findViewById<TextView>(R.id.txt_view).text = "Connection successful"
        }.addOnFailureListener { exception ->
            // Connection failed
            findViewById<TextView>(R.id.txt_view).text = "Connection failed: ${exception.message}"
        }
    }
}