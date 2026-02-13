package com.imashnake.animite.api.anilist

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject
import kotlin.collections.filterNotNull
import kotlin.collections.orEmpty

/**
 * Repository for fetching [MediaQuery.Media] or a list of [MediaListQuery.Medium].
 *
 * @param apolloClient Default apollo client.
 * @property fetchMediaList Fetches a list of [MediaListQuery.Medium].
 * @property fetchMedia Fetches detailed media: [MediaQuery.Media].
 */
@Suppress("LongParameterList")
class AnilistMediaRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {

    fun fetchMediaList(
        mediaListType: MediaList.Type,
        mediaType: MediaType,
        sort: List<MediaSort>,
        useNetwork: Boolean,
        page: Int = 0,
        perPage: Int = 10,
        season: MediaSeason? = null,
        seasonYear: Int? = null
    ): Flow<Result<MediaList>> {
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
            .fetchPolicy(
                fetchPolicy = if (useNetwork) {
                    FetchPolicy.NetworkFirst
                } else FetchPolicy.CacheFirst
            )
            .toFlow()
            .filter { it.exception == null }
            .asResult {
                MediaList(
                    type = mediaListType,
                    list = it.page!!.media.orEmpty().filterNotNull().map { query ->
                        Media.Small(query.mediaSmall)
                    }.toImmutableList()
                )
            }
    }

    fun fetchMediaMediumList(
        mediaType: MediaType,
        sort: List<MediaSort>,
        page: Int = 0,
        perPage: Int = 10,
        genre: String,
    ): Flow<Result<List<Media.Medium>>> {
        return apolloClient
            .query(
                MediaMediumListQuery(
                    type = Optional.presentIfNotNull(mediaType),
                    page = Optional.presentIfNotNull(page),
                    perPage = Optional.presentIfNotNull(perPage),
                    sort = Optional.presentIfNotNull(sort),
                    genre = Optional.presentIfNotNull(genre)
                )
            )
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .toFlow()
            .filter { it.exception == null }
            .asResult {
                it.page!!.media.orEmpty().filterNotNull().map { query ->
                    Media.Medium(query.mediaMedium)
                }
            }
    }

    fun fetchMedia(
        id: Int?,
        mediaType: MediaType,
        recommendationCount: Int = 10
    ): Flow<Result<Media>> {
        return apolloClient
            .query(
                MediaQuery(
                    id = Optional.presentIfNotNull(id),
                    type = Optional.presentIfNotNull(mediaType),
                    recommendationsPerPage = Optional.presentIfNotNull(recommendationCount)
                )
            )
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .toFlow()
            .filter { it.exception == null }
            .asResult { Media(it.media!!) }
    }
}
