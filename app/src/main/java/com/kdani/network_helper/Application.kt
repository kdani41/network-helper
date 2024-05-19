package com.kdani.network_helper

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
internal class MainApplication : Application() {

    init {
        Timber.plant(Timber.DebugTree())
    }
}