package dev.murod.connectiontestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.murod.connectiontestapp.service.MessageClientConnectivity
import dev.murod.connectiontestapp.service.MessageClientListener
import dev.murod.connectiontestapp.service.MessageClientService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val msgClientService: MessageClientService) : ViewModel() {

    private val _connectivityState =
        MutableStateFlow<MessageClientConnectivity>(msgClientService.getConnectivityState())
    val connectivityState: StateFlow<MessageClientConnectivity> = _connectivityState

    private val _msgSentState = MutableStateFlow<Boolean>(false)
    val msgSentState: StateFlow<Boolean> = _msgSentState

    private var msgCount: Int = 0


    init {
        msgClientService.addListener(object : MessageClientListener {
            override fun onClientConnectivity(state: MessageClientConnectivity) {
                _connectivityState.value = state
            }

            override fun onMessageSent() {
                _msgSentState.value = true
            }
        })
        msgClientService.prepareConnection()
    }

    fun sentMessage() {
        _msgSentState.value = false
        msgCount++
        msgClientService.sendMessage("Message: $msgCount")
    }

    class Factory(private val msgClientService: MessageClientService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(msgClientService) as T
        }
    }
}