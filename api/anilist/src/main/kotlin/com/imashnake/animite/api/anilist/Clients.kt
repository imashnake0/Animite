package com.imashnake.animite.api.anilist

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

private const val BaseUrl = "https://graphql.anilist.co/"
private val MemoryCacheFactory = MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)
// Despite being a "factory", this will create the driver at call time
private val SqlCacheFactory
    get() = SqlNormalizedCacheFactory("apollo.db")

/**
 * Creates an [ApolloClient] configured to access AniList APIs.
 */
 fun createApolloHttpClient(): ApolloClient {
    val cacheFactory = MemoryCacheFactory.chain(SqlCacheFactory)
    return ApolloClient.Builder()
        .dispatcher(Dispatchers.IO)
        .serverUrl(BaseUrl)
        .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
        .normalizedCache(cacheFactory)
        .build()
}

/**
 * Creates an authenticated [ApolloClient] configured to access AniList APIs using authentication
 * details from [PreferencesRepository].
 */
fun createAuthenticatedApolloHttpClient(
    preferencesRepository: PreferencesRepository
): ApolloClient {
    val httpInterceptor = object : HttpInterceptor {
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
    val cacheFactory = MemoryCacheFactory.chain(SqlCacheFactory)
    return ApolloClient.Builder()
        .dispatcher(Dispatchers.IO)
        .serverUrl(BaseUrl)
        .addHttpInterceptor(httpInterceptor)
        .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
        .normalizedCache(cacheFactory)
        .build()
}
