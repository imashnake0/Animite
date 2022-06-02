package com.imashnake.animite.data.sauce

import com.imashnake.animite.AnimeQuery
import com.imashnake.animite.data.sauce.apis.AnimeApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
class AnimeNetworkSource @Inject constructor(
    private val animeApi: AnimeApi,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun fetchAnime(id: Int): AnimeQuery.Media? =
        withContext(dispatcher) {
            animeApi.fetchAnime(id)
        }
}
