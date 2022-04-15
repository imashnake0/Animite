package com.imashnake.animite.data.repos

import com.imashnake.animite.AnimeQuery
import com.imashnake.animite.data.sauce.AnimeNetworkSource

/**
 * TODO: Kdoc.
 */
class AnimeRepository(
    private val animeNetworkSource: AnimeNetworkSource
) {
    suspend fun fetchAnime(id: Int): AnimeQuery.Media? =
        animeNetworkSource.fetchAnime(id)
}
