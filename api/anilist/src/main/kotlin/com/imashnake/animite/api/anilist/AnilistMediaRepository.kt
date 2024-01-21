package com.imashnake.animite.api.anilist

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository for fetching [MediaQuery.Media] or a list of [MediaListQuery.Medium].
 *
 * @param apolloClient Default apollo client.
 * @property fetchMediaList Fetches a list of [MediaListQuery.Medium].
 * @property fetchMedia Fetches detailed media: [MediaQuery.Media].
 */
@Suppress("detekt:LongParameterList")
class AnilistMediaRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {

    fun fetchMediaList(
        mediaType: MediaType,
        sort: List<MediaSort>,
        page: Int = 0,
        perPage: Int = 10,
        season: MediaSeason? = null,
        seasonYear: Int? = null
    ): Flow<Result<List<Media.Medium>>> {
        return apolloClient
            .query(
                MediaListQuery(
                    type = Optional.presentIfNotNull(mediaType),
                    page = Optional.presentIfNotNull(page),
                    perPage = Optional.presentIfNotNull(perPage),
                    sort = Optional.presentIfNotNull(sort),
                    season = Optional.presentIfNotNull(season),
                    seasonYear = Optional.presentIfNotNull(seasonYear)
                )
            )
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .toFlow()
            .asResult {
                it.page!!.media.orEmpty().filterNotNull().map { query -> Media.Medium(query) }
            }
    }

    fun fetchMedia(id: Int?, mediaType: MediaType): Flow<Result<Media>> {
        return apolloClient
            .query(
                MediaQuery(
                    id = Optional.presentIfNotNull(id),
                    type = Optional.presentIfNotNull(mediaType)
                )
            )
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .toFlow()
            .asResult { Media(it.media!!) }
    }
}
