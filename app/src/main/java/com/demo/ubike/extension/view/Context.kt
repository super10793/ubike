package com.demo.ubike.extension.view

import android.content.Context
import android.provider.Settings

fun Context.dpToPx(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.dpToPx(dp: Float): Float {
    val scale = resources.displayMetrics.density
    return dp * scale
}

fun Context.getDimen(id: Int): Int {
    return resources.getDimensionPixelSize(id)
}

fun Context.getDeviceId(): String {
    return Settings.Secure.getString(
        this.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}