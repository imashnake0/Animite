package com.imashnake.animite.api.anilist

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo3.network.http.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnilistApiModule {

    // TODO: Name this so we can have other apollo clients for different APIs.
    @Provides
    @Singleton
    fun provideApolloClient(
        @ApplicationContext context: Context
    ): ApolloClient {
        // Cache is hit in order, so check in-memory -> check sqlite
        // We have an in-memory cache first for speed, then a SQLite cache for persistence.
        val cacheFactory = MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)
            .chain(SqlNormalizedCacheFactory(context, "apollo.db"))
        return ApolloClient.Builder()
            .dispatcher(Dispatchers.IO)
            .serverUrl("https://graphql.anilist.co/")
            .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
            .normalizedCache(cacheFactory)
            .build()
    }
}
