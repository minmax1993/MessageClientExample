package dev.murod.connectiontestapp.app

import android.app.Application
import dev.murod.connectiontestapp.di.Injection

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Injection.init(this)
    }
}