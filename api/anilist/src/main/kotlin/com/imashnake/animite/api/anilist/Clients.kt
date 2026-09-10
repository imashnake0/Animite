package com.imashnake.animite.api.anilist

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.apollographql.apollo.network.http.LoggingInterceptor
import com.apollographql.cache.normalized.memory.MemoryCacheFactory
import com.apollographql.cache.normalized.sql.SqlNormalizedCacheFactory
import com.imashnake.animite.api.anilist.cache.Cache.cache
import com.imashnake.animite.api.preferences.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

private const val BaseUrl = "https://graphql.anilist.co/"
private val MemoryCacheFactory = MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)
// Despite being a "factory", this will create the driver at call time
private val SqlCacheFactory
    get() = SqlNormalizedCacheFactory("apollo.db")

/**
 * Adds the Referer header to every AniList API request.
 *
 * AniList currently requires this header for requests from third-party
 * clients. Without it, otherwise valid API requests may be rejected with
 * an HTTP 403 response.
 *
 * This behavior is not part of the documented API contract and may change
 * in the future. Keeping the header handling in a dedicated interceptor
 * makes it easy to update or remove if AniList changes its requirements.
 */
private val aniListRefererInterceptor = object : HttpInterceptor {
    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {
        return chain.proceed(
            request.newBuilder()
                .addHeader("Referer", "https://anilist.co/")
                .build()
        )
    }
}

/**
 * Creates an [ApolloClient] configured to access AniList APIs.
 */
fun createApolloHttpClient(): ApolloClient {
    val cacheFactory = MemoryCacheFactory.chain(SqlCacheFactory)
    return ApolloClient.Builder()
        .dispatcher(Dispatchers.IO)
        .serverUrl(BaseUrl)
        .addHttpInterceptor(aniListRefererInterceptor)
        .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
        .cache(cacheFactory)
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
                    preferencesRepository.accessToken.firstOrNull()?.let {
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
        .addHttpInterceptor(aniListRefererInterceptor)
        .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
        .cache(cacheFactory)
        .build()
}
