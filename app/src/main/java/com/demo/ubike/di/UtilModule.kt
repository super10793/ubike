package com.demo.ubike.di

import android.content.Context
import com.demo.ubike.utils.FirebaseAnalyticsUtil
import com.demo.ubike.utils.NetworkUtil
import com.demo.ubike.utils.SharePreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    private const val PREFERENCE_NAME = "app_preferences"

    @Singleton
    @Provides
    fun provideSharedPreferencesManager(@ApplicationContext context: Context): SharePreferenceManager {
        val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return SharePreferenceManager(prefs)
    }

    @Singleton
    @Provides
    fun provideNetworkUtil(@ApplicationContext context: Context): NetworkUtil {
        return NetworkUtil(context)
    }

    @Singleton
    @Provides
    fun provideFirebaseAnalyticsUtil(@ApplicationContext context: Context): FirebaseAnalyticsUtil {
        return FirebaseAnalyticsUtil(context)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setPrettyPrinting()
            .create()
    }
}