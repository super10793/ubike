package com.demo.ubike.di

import com.demo.ubike.data.repository.DataStoreRepository
import com.demo.ubike.data.repository.DataStoreRepositoryImpl
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.data.repository.HomeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindDataStoreRepository(impl: DataStoreRepositoryImpl): DataStoreRepository
}