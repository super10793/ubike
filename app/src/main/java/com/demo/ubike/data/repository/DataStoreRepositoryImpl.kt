package com.demo.ubike.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) : DataStoreRepository {

    companion object {
        val TOKEN_LIST = stringPreferencesKey("token_list")
    }

    override val tokensFlow: Flow<List<String>>
        get() = dataStore.data.map { prefs ->
            val json = prefs[TOKEN_LIST] ?: "[]"
            gson.fromJson(json, object : TypeToken<List<String>>() {}.type)
        }

    override suspend fun saveTokens(tokens: List<String>) {
        val json = gson.toJson(tokens)
        dataStore.edit { prefs ->
            prefs[TOKEN_LIST] = json
        }
    }
}