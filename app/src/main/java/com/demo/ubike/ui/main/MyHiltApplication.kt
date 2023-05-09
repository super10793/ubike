package com.demo.ubike.ui.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyHiltApplication : Application() {
    @Inject
    lateinit var flipperManager: FlipperManager
    override fun onCreate() {
        super.onCreate()
        flipperManager.init()
    }
}