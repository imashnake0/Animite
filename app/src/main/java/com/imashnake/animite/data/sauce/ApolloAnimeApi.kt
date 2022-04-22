package com.imashnake.animite.data.sauce

import com.apollographql.apollo3.api.Optional
import com.imashnake.animite.AnimeQuery
import com.imashnake.animite.AnimeQuery.Media
import javax.inject.Inject

/**
 * TODO: Kdoc.
 *
 * Example:
 *
 * **Anime:** Sono Bisque Doll wa Koi wo Suru;
 * **ID:** 132405.
 */
class ApolloAnimeApi @Inject constructor() : AnimeApi {
    override suspend fun fetchAnime(id: Int): Media? {
        return client
            .query(
                AnimeQuery(id = Optional.presentIfNotNull(id))
            )
            .execute().data?.media
    }
}
