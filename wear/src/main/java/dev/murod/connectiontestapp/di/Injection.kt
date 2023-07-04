package dev.murod.connectiontestapp.di

import android.content.Context
import dev.murod.connectiontestapp.connectivity.MessageClientService

object Injection {
    lateinit var msgClientService: MessageClientService

    fun init(context: Context) {
        msgClientService = MessageClientService(context)
    }
}