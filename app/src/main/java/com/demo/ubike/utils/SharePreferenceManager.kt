package com.demo.ubike.utils

import android.content.SharedPreferences
import javax.inject.Inject

class SharePreferenceManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val TOKEN = "TOKEN"
    }

    var token: String
        get() = getString(TOKEN)
        set(value) = setString(TOKEN, value)

    private fun getString(name: String): String {
        return sharedPreferences.getString(name, "") ?: ""
    }

    private fun setString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
}