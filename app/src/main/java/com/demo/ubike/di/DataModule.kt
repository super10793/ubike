package com.demo.ubike.di

import com.demo.ubike.usecase.FetchTokenUseCase
import com.demo.ubike.data.repository.HomeRepository
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
    fun provideFetchTokenUseCase(homeRepository: HomeRepository): FetchTokenUseCase {
        return FetchTokenUseCase(homeRepository)
    }
}