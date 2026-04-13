package com.imashnake.animite.api.anilist

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.Media.Season.Companion.sanitize
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.api.anilist.type.MediaFormat
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaStatus
import com.imashnake.animite.api.anilist.type.MediaType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

// TODO: Add preference for adult content.
private const val HENTAI = "Hentai"

/**
 * Repository for fetching [MediaQuery.Media] or a list of [MediaListQuery.Medium].
 *
 * @param apolloClient Default apollo client.
 * @property fetchMediaList Fetches a list of [MediaListQuery.Medium].
 * @property fetchMedia Fetches detailed media: [MediaQuery.Media].
 */
@Suppress("LongParameterList")
class AnilistMediaRepository(
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
                    }.toImmutableList(),
                    season = season?.sanitize(),
                    year = seasonYear,
                )
            }
    }

    fun fetchMediaMediumList(
        mediaType: MediaType,
        sort: List<MediaSort>,
        page: Int = 0,
        perPage: Int = 10,
        genre: String? = null,
        includedGenres: List<String>? = null,
        excludedGenres: List<String>? = null,
        season: MediaSeason? = null,
        year: Int? = null,
        includedFormats: List<MediaFormat>? = null,
        excludedFormats: List<MediaFormat>? = null,
        includedStatuses: List<MediaStatus>? = null,
        excludedStatuses: List<MediaStatus>? = null,
        search: String? = null,
    ): Flow<Result<ImmutableList<Media.Medium>>> {
        return apolloClient
            .query(
                MediaMediumListQuery(
                    type = Optional.presentIfNotNull(mediaType),
                    page = Optional.presentIfNotNull(page),
                    perPage = Optional.presentIfNotNull(perPage),
                    sort = Optional.presentIfNotNull(sort),
                    genre = Optional.presentIfNotNull(genre),
                    genreIn = Optional.presentIfNotNull(includedGenres),
                    genreNotIn = Optional.presentIfNotNull(excludedGenres),
                    search = Optional.presentIfNotNull(search),
                    season = Optional.presentIfNotNull(season),
                    // Start FuzzyDateInt has to be YYYYMMDD -> YYYY0101
                    startDate = Optional.presentIfNotNull(year?.times(10000)?.plus(101)),
                    // End FuzzyDateInt has to be YYYYMMDD -> YYYY1231
                    endDate = Optional.presentIfNotNull(year?.times(10000)?.plus(1231)),
                    formatIn = Optional.presentIfNotNull(includedFormats),
                    formatNotIn = Optional.presentIfNotNull(excludedFormats),
                    statusIn = Optional.presentIfNotNull(includedStatuses),
                    statusNotIn = Optional.presentIfNotNull(excludedStatuses),
                )
            )
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .toFlow()
            .filter { it.exception == null }
            .asResult {
                it.page!!.media.orEmpty().filterNotNull().map { query ->
                    Media.Medium(query.mediaMedium)
                }.toImmutableList()
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

    suspend fun fetchMediaGenres() = apolloClient
        .query(GenresQuery())
        .fetchPolicy(FetchPolicy.CacheFirst)
        .execute()
        .data
        ?.GenreCollection
        ?.filterNotNull()
        ?.filterNot { genre -> genre == HENTAI }
        .orEmpty()
}
