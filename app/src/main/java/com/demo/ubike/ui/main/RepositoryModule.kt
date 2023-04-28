package com.demo.ubike.ui.main

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