package dev.murod.connectiontestapp.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.wearable.MessageEvent
import dev.murod.connectiontestapp.connectivity.MessageClientService

class MainViewModel(msgClientService: MessageClientService) : ViewModel() {

    val messageState = mutableStateOf<MessageEvent?>(null)

    init {
        msgClientService.addListener { message ->
            val path = message.path
            val data = message.data
            messageState.value = message
        }
    }


    class Factory(private val msgClientService: MessageClientService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(msgClientService) as T
        }
    }
}