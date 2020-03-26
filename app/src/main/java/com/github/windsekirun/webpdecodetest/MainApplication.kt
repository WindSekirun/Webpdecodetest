package com.github.windsekirun.webpdecodetest

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}