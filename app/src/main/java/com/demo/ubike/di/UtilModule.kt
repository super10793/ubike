package com.demo.ubike.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.demo.ubike.utils.FirebaseAnalyticsUtil
import com.demo.ubike.utils.NetworkUtil
import com.demo.ubike.utils.SharePreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences): SharePreferenceManager {
        return SharePreferenceManager(sharedPreferences)
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
}