package com.imashnake.animite.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.imashnake.animite.dev.internal.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun providesNetworkDispatcher(): CoroutineDispatcher = Dispatchers.IO

    /*
    todo apollo cache
    @Provides
    fun provideCacheFactory() = LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024 * 1024).build())*/


    @Provides
    @Singleton
    fun provideApolloClient(dispatcher: CoroutineDispatcher): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(Constants.ANILIST_BASE_URL)
            .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
            .dispatcher(dispatcher)
            .build()
    }
}