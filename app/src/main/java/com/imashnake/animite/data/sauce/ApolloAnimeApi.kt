package com.imashnake.animite.data.sauce

import com.apollographql.apollo3.api.Optional
import com.imashnake.animite.AnimeQuery
import com.imashnake.animite.AnimeQuery.Media

/**
 * TODO:
 *  * Kdoc.
 *  * Un-hardcode id.
 *
 * Example:
 *
 * **Anime:** Sono Bisque Doll wa Koi wo Suru;
 * **ID:** 132405.
 */
class ApolloAnimeApi : AnimeApi {
    override suspend fun fetchAnime(): Media? {
        return client
            .query(
                AnimeQuery(id = Optional.presentIfNotNull(132405))
            )
            .execute().data?.media
    }
}
