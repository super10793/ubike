package com.demo.ubike.utils

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.demo.ubike.extension.view.getDeviceId
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FirebaseAnalyticsUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    companion object {
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
        val deviceId = context.getDeviceId()
        firebaseAnalytics.setUserId(deviceId)
    }

    private fun logEvent(name: String, event: Bundle) {
        firebaseAnalytics.logEvent(name, event)
    }
}