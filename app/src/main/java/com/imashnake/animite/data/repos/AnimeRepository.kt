package com.imashnake.animite.data.repos

import com.imashnake.animite.AnimeQuery
import com.imashnake.animite.data.sauce.AnimeNetworkSource
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
class AnimeRepository @Inject constructor(
    private val animeNetworkSource: AnimeNetworkSource
) {
    suspend fun fetchAnime(id: Int): AnimeQuery.Media? =
        animeNetworkSource.fetchAnime(id)
}
