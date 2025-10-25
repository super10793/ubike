package com.demo.ubike.utils

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.demo.ubike.BuildConfig
import com.demo.ubike.extension.view.getAppDeviceId
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FirebaseAnalyticsUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    companion object {
        private const val DEBUG_SUFFIX = "_debug"
        private const val MARKER_CLICK = "marker_click_event"
        private const val FAVORITE_ADD_CLICK = "favorite_add_click_event"
        private const val FAVORITE_REMOVE_CLICK = "favorite_remove_click_event"
        private const val SUPPORT_CITY_CLICK = "support_city_click_event"
        private const val GO_TO_SETTING_CLICK = "go_to_setting_click_event"

        private const val EXCEPTION_EVENT = "exception_event"

        private const val EVENT_KEY_STATION_UID = "stationUid"

        private const val EVENT_KEY_STATION_NAME = "stationName"

        private const val EVENT_KEY_CITY_NAME = "cityName"

        private const val EVENT_KEY_USER_EVENT = "userEvent"

        private const val EVENT_KEY_EXCEPTION = "appException"
    }

    init {
        firebaseAnalytics.setAnalyticsCollectionEnabled(true)
    }

    fun markerClickEvent(stationUid: String, stationName: String) {
        logEvent(
            MARKER_CLICK, bundleOf(
                EVENT_KEY_STATION_UID to stationUid,
                EVENT_KEY_STATION_NAME to stationName
            )
        )
    }

    fun favoriteAddClickEvent(stationUid: String, stationName: String) {
        logEvent(
            FAVORITE_ADD_CLICK, bundleOf(
                EVENT_KEY_STATION_UID to stationUid,
                EVENT_KEY_STATION_NAME to stationName
            )
        )
    }

    fun favoriteRemoveClickEvent(stationUid: String, stationName: String) {
        logEvent(
            FAVORITE_REMOVE_CLICK, bundleOf(
                EVENT_KEY_STATION_UID to stationUid,
                EVENT_KEY_STATION_NAME to stationName
            )
        )
    }

    fun supportCityClickEvent(cityName: String) {
        logEvent(
            SUPPORT_CITY_CLICK, bundleOf(
                EVENT_KEY_CITY_NAME to cityName
            )
        )
    }

    fun goToSettingClick() {
        logEvent(
            GO_TO_SETTING_CLICK, bundleOf(
                EVENT_KEY_USER_EVENT to GO_TO_SETTING_CLICK
            )
        )
    }

    fun exceptionEvent(message: String) {
        logEvent(
            EXCEPTION_EVENT, bundleOf(
                EVENT_KEY_EXCEPTION to message
            )
        )
    }

    fun loginEvent() {
        val deviceId: String =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                context.deviceId.toString()
            } else {
                context.getAppDeviceId()
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