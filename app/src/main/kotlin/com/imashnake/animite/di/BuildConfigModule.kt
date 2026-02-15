package com.imashnake.animite.di

import com.imashnake.animite.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BuildConfigModule {

    @Provides
    @Singleton
    fun provideVersionName() = BuildConfig.VERSION_NAME
}