package com.demo.ubike.di

import android.content.Context
import com.demo.ubike.data.local.AppDatabase
import com.demo.ubike.data.local.favorite.FavoriteDao
import com.demo.ubike.data.local.station.StationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.buildDatabase(context)
    }

    @Singleton
    @Provides
    fun provideStationDao(
        appDatabase: AppDatabase
    ): StationDao {
        return appDatabase.stationDao()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(
        appDatabase: AppDatabase
    ): FavoriteDao {
        return appDatabase.favoriteDao()
    }
}