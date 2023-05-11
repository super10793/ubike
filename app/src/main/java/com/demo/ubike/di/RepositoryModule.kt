package com.demo.ubike.di

import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.data.repository.HomeRepositoryImpl
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
    fun provideHomeRepositoryImpl(homeApi: HomeApi): HomeRepository {
        return HomeRepositoryImpl(homeApi)
    }
}