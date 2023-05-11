package com.demo.ubike.utils

import android.content.Context
import com.demo.ubike.BuildConfig
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FlipperManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val inspectorPlugin: InspectorFlipperPlugin,
    private val networkPlugin: NetworkFlipperPlugin,
    private val databasesPlugin: DatabasesFlipperPlugin,
    private val sharedPreferencesPlugin: SharedPreferencesFlipperPlugin,
    private val crashReporterPlugin: CrashReporterPlugin,
) {
    fun init() {
        SoLoader.init(context, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(context)) {
            val client = AndroidFlipperClient.getInstance(context)
            client.addPlugin(inspectorPlugin)
            client.addPlugin(networkPlugin)
            client.addPlugin(databasesPlugin)
            client.addPlugin(sharedPreferencesPlugin)
            client.addPlugin(crashReporterPlugin)
            client.start()
        }
    }
}