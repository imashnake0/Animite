package com.imashnake.animite.di

import com.apollographql.apollo3.ApolloClient
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

    @Provides
    @Singleton
    fun provideApolloClient(dispatcher: CoroutineDispatcher): ApolloClient {
        return ApolloClient.Builder()
            .dispatcher(dispatcher)
            .serverUrl(Constants.ANILIST_BASE_URL)
            .build()
    }
}
