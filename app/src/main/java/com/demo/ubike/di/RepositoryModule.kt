package com.demo.ubike.di

import android.content.Context
import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.local.favorite.FavoriteDao
import com.demo.ubike.data.local.station.StationDao
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.data.repository.HomeRepositoryImpl
import com.demo.ubike.utils.SharePreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideHomeRepositoryImpl(
        @ApplicationContext context: Context,
        sharePreferenceManager: SharePreferenceManager,
        homeApi: HomeApi,
        stationDao: StationDao,
        favoriteDao: FavoriteDao,
    ): HomeRepository {
        return HomeRepositoryImpl(
            context,
            sharePreferenceManager,
            homeApi,
            stationDao,
            favoriteDao
        )
    }
}