package com.demo.ubike

import android.app.Application
import com.demo.ubike.utils.FlipperManager
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