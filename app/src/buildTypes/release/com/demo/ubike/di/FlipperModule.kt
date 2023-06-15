package com.demo.ubike.di

import com.demo.ubike.utils.FlipperManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlipperModule {
    @Singleton
    @Provides
    fun provideFlipperManager(): FlipperManager = FlipperManager()

    @Singleton
    @Provides
    @FlipperInterceptor
    fun provideFlipperInterceptor(): Interceptor? = null
}