package com.imashnake.animite.data.sauce

import com.imashnake.animite.AnimeQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * TODO: Kdoc.
 */
class AnimeNetworkSource(
    private val animeApi: AnimeApi,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun fetchAnime(): AnimeQuery.Media? =
        withContext(dispatcher) {
            animeApi.fetchAnime()
        }
}
