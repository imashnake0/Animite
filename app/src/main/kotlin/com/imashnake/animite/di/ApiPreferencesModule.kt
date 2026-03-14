package com.imashnake.animite.di

import android.content.Context
import com.imashnake.animite.api.preferences.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiPreferencesModule {
    @Singleton
    @Provides
    fun providePreferencesRepository(@ApplicationContext appContext: Context): PreferencesRepository {
        return PreferencesRepository(appContext)
    }
}