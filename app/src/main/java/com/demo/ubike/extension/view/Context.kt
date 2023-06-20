package com.demo.ubike.extension.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ToastUtils
import com.demo.ubike.R

fun Context.dpToPx(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.dpToPx(dp: Float): Float {
    val scale = resources.displayMetrics.density
    return dp * scale
}

fun Context.showRouteInGoogleMap(lat: Double, lon: Double) {
    val gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=&daddr=$lat,$lon")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
    } else {
        ToastUtils.cancel()
        ToastUtils.make()
            .setBgColor(ContextCompat.getColor(this, R.color.toast_bg))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setGravity(Gravity.BOTTOM, 0, this.dpToPx(60))
            .setDurationIsLong(false)
            .show(R.string.google_map_not_found)
    }
}

fun Context.getDeviceId(): String {
    return Settings.Secure.getString(
        this.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}