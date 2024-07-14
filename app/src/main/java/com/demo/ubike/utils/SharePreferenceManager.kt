package com.demo.ubike.utils

import android.content.SharedPreferences
import javax.inject.Inject

class SharePreferenceManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val TOKENS = "tokens"
    }

    fun randomToken(): String = getStringSet(TOKENS).random()

    var tokens: Set<String>
        get() = getStringSet(TOKENS)
        set(value) = putStringSet(TOKENS, value)

    private fun getString(name: String): String {
        return sharedPreferences.getString(name, "") ?: ""
    }

    private fun setString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getStringSet(key: String): Set<String> {
        return sharedPreferences.getStringSet(key, null) ?: setOf()
    }

    private fun putStringSet(key: String, value: Set<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet(key, value)
        editor.apply()
    }
}