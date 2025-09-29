package com.demo.ubike.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.local.favorite.FavoriteDao
import com.demo.ubike.data.local.station.StationDao
import com.demo.ubike.data.repository.DataStoreRepository
import com.demo.ubike.data.repository.DataStoreRepositoryImpl
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.data.repository.HomeRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideHomeRepositoryImpl(
        homeApi: HomeApi,
        stationDao: StationDao,
        favoriteDao: FavoriteDao,
    ): HomeRepository {
        return HomeRepositoryImpl(
            homeApi,
            stationDao,
            favoriteDao
        )
    }

    @Singleton
    @Provides
    fun provideDataStoreRepositoryImpl(
        dataStore: DataStore<Preferences>,
        gson: Gson
    ): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore, gson)
    }
}