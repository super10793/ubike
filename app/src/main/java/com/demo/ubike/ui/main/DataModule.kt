package com.demo.ubike.ui.main

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideFetchFakeDataUseCase(homeRepository: HomeRepository): FetchFakeDataUseCase {
        return FetchFakeDataUseCase(homeRepository)
    }
}