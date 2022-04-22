@file:Suppress("Unused")

package com.imashnake.animite.di

import com.imashnake.animite.data.sauce.AnimeApi
import com.imashnake.animite.data.sauce.ApolloAnimeApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun providesNetworkDispatcher(): CoroutineDispatcher =
        Dispatchers.IO
}

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkModuleBinds {
    @Singleton
    @Binds
    abstract fun providesAnimeApi(
        apolloAnimeApi: ApolloAnimeApi
    ): AnimeApi
}
