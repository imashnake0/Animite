package com.imashnake.animite.api.anilist

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.apollographql.apollo.network.http.LoggingInterceptor
import com.imashnake.animite.api.preferences.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnilistApiModule {
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

    @Provides
    @Singleton
    @AuthorizedClient
    fun provideAuthorizedApolloClient(
        @ApplicationContext context: Context,
        httpInterceptor: HttpInterceptor
    ): ApolloClient {
        val cacheFactory = MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)
            .chain(SqlNormalizedCacheFactory(context, "apollo.db"))
        return ApolloClient.Builder()
            .dispatcher(Dispatchers.IO)
            .serverUrl("https://graphql.anilist.co/")
            .addHttpInterceptor(httpInterceptor)
            .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
            .normalizedCache(cacheFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpInterceptor(
        preferencesRepository: PreferencesRepository
    ): HttpInterceptor = object : HttpInterceptor {
        override suspend fun intercept(
            request: HttpRequest,
            chain: HttpInterceptorChain
        ): HttpResponse {
            return chain.proceed(
                request.newBuilder().apply {
                    preferencesRepository.accessToken.first()?.let {
                        addHeader("Authorization", "Bearer $it")
                    }
                }.build()
            )
        }
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthorizedClient
