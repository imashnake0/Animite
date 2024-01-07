package com.imashnake.animite.api.anilist

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.imashnake.animite.core.GlobalVariables
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
        @ApplicationContext context: Context,
        globalVariables: GlobalVariables
    ): ApolloClient {
        // Cache is hit in order, so check in-memory -> check sqlite
        // We have an in-memory cache first for speed, then a SQLite cache for persistence.
        val cacheFactory = MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)
            .chain(SqlNormalizedCacheFactory(context, "apollo.db"))
        return ApolloClient.Builder()
            .dispatcher(Dispatchers.IO)
            .serverUrl("https://graphql.anilist.co/")
            .addHttpInterceptor(AuthorizationInterceptor(globalVariables.accessToken))
            .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
            .normalizedCache(cacheFactory)
            .build()
    }

    class AuthorizationInterceptor(private val token: String?) : HttpInterceptor {
        override suspend fun intercept(request: HttpRequest, chain: HttpInterceptorChain): HttpResponse {
            return chain.proceed(
                request.newBuilder().apply {
                    token?.let {
                        addHeader("Authorization", "Bearer $it")
                    }
                }.build()
            )
        }
    }
}
