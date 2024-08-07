package com.demo.ubike.utils

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.core.os.bundleOf
import com.demo.ubike.BuildConfig
import com.demo.ubike.extension.view.getDeviceId
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FirebaseAnalyticsUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    companion object {
        private const val DEBUG_SUFFIX = "_debug"
        const val MARKER_CLICK = "marker_click_event"
        const val FAVORITE_ADD_CLICK = "favorite_add_click_event"
        const val FAVORITE_REMOVE_CLICK = "favorite_remove_click_event"
        const val SUPPORT_CITY_CLICK = "support_city_click_event"
        const val GO_TO_SETTING_CLICK = "go_to_setting_click_event"
    }

    init {
        firebaseAnalytics.setAnalyticsCollectionEnabled(true)
    }

    fun markerClickEvent(stationUid: String, stationName: String) {
        logEvent(
            MARKER_CLICK, bundleOf(
                "stationUid" to stationUid,
                "stationName" to stationName
            )
        )
    }

    fun favoriteAddClickEvent(stationUid: String, stationName: String) {
        logEvent(
            FAVORITE_ADD_CLICK, bundleOf(
                "stationUid" to stationUid,
                "stationName" to stationName
            )
        )
    }

    fun favoriteRemoveClickEvent(stationUid: String, stationName: String) {
        logEvent(
            FAVORITE_REMOVE_CLICK, bundleOf(
                "stationUid" to stationUid,
                "stationName" to stationName
            )
        )
    }

    fun supportCityClickEvent(cityName: String) {
        logEvent(
            SUPPORT_CITY_CLICK, bundleOf(
                "cityName" to cityName
            )
        )
    }

    fun goToSettingClick() {
        logEvent(
            GO_TO_SETTING_CLICK, bundleOf(
                "userEvent" to GO_TO_SETTING_CLICK
            )
        )
    }

    fun loginEvent() {
        val deviceId: String = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            context.deviceId.toString()
        } else {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        firebaseAnalytics.setUserId(deviceId)
    }

    private fun logEvent(name: String, event: Bundle) {
        val eventName = if (BuildConfig.DEBUG) {
            "$name$DEBUG_SUFFIX"
        } else {
            name
        }
        firebaseAnalytics.logEvent(eventName, event)
    }
}