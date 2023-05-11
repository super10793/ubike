package com.demo.ubike.di

import android.content.Context
import com.demo.ubike.utils.FlipperManager
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlipperModule {
    @Singleton
    @Provides
    fun provideFlipperManager(
        @ApplicationContext context: Context,
        inspectorPlugin: InspectorFlipperPlugin,
        networkPlugin: NetworkFlipperPlugin,
        databasesPlugin: DatabasesFlipperPlugin,
        sharedPreferencesPlugin: SharedPreferencesFlipperPlugin,
        crashReporterPlugin: CrashReporterPlugin
    ): FlipperManager {
        return FlipperManager(
            context,
            inspectorPlugin,
            networkPlugin,
            databasesPlugin,
            sharedPreferencesPlugin,
            crashReporterPlugin
        )
    }

    @Singleton
    @Provides
    fun provideInspectorFlipperPlugin(
        @ApplicationContext context: Context
    ): InspectorFlipperPlugin {
        return InspectorFlipperPlugin(
            context,
            DescriptorMapping.withDefaults()
        )
    }

    @Singleton
    @Provides
    fun provideNetworkFlipperPlugin(): NetworkFlipperPlugin {
        return NetworkFlipperPlugin()
    }

    @Singleton
    @Provides
    fun provideDatabasesFlipperPlugin(
        @ApplicationContext context: Context
    ): DatabasesFlipperPlugin {
        return DatabasesFlipperPlugin(context)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesFlipperPlugin(
        @ApplicationContext context: Context
    ): SharedPreferencesFlipperPlugin {
        return SharedPreferencesFlipperPlugin(context)
    }

    @Singleton
    @Provides
    fun provideCrashReporterPlugin(): CrashReporterPlugin {
        return CrashReporterPlugin.getInstance()
    }
}