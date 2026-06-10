package com.imashnake.animite.api.anilist

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.fetchPolicy
import com.imashnake.animite.api.anilist.sanitize.explore.FilterStrategy
import com.imashnake.animite.api.anilist.sanitize.media.Info
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.api.anilist.sanitize.media.Page
import com.imashnake.animite.api.anilist.type.MediaType
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

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
    private val apolloClient: ApolloClient,
) {

    fun fetchMediaList(
        title: String,
        useNetwork: Boolean,
        filterStrategy: FilterStrategy
    ): Flow<Result<MediaList>> = with(filterStrategy) {
        apolloClient.query(
            MediaListQuery(
                type = Optional.presentIfNotNull(mediaType),
                page = Optional.presentIfNotNull(page),
                perPage = Optional.presentIfNotNull(perPage),
                sort = Optional.presentIfNotNull(sort),
                season = Optional.presentIfNotNull(season),
                seasonYear = Optional.presentIfNotNull(year),
                isAdult = Optional.presentIfNotNull(if (isNsfwEnabled) null else false)
            )
        )
        .fetchPolicy(
            if (useNetwork) {
                FetchPolicy.NetworkFirst
            } else FetchPolicy.CacheFirst
        )
        .toFlow()
        .filter { it.exception == null }
        .asResult {
            MediaList(
                title = title,
                list = it.page!!.media.orEmpty().filterNotNull().map { query ->
                    Media.Small(query.mediaSmall, language)
                }.toImmutableList(),
                filterStrategy = this,
            )
        }
    }

    fun fetchMediaMediumList(
        useNetwork: Boolean,
        filterStrategy: FilterStrategy
    ): Flow<Result<Page<Media.Medium>>> = with(filterStrategy) {
        apolloClient.query(
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
                isAdult = Optional.presentIfNotNull(
                    if (isNsfwEnabled) {
                        if (isAdult) true else null
                    } else false
                )
            )
        )
        .fetchPolicy(
            if (useNetwork) {
                FetchPolicy.NetworkFirst
            } else FetchPolicy.CacheFirst
        )
        .toFlow()
        .filter { it.exception == null }
        .asResult { data ->
            Page(
                list = data.page!!.media.orEmpty().filterNotNull().map { query ->
                    Media.Medium(query.mediaMedium, language)
                }.toImmutableList(),
                info = data.page.pageInfo?.let { Info(it) }
            )
        }
    }

    fun fetchMedia(
        id: Int?,
        mediaType: MediaType,
        recommendationCount: Int = 10,
        language: Media.Language = Media.Language.DEFAULT,
    ): Flow<Result<Media>> {
        return apolloClient.query(
            MediaQuery(
                id = Optional.presentIfNotNull(id),
                type = Optional.presentIfNotNull(mediaType),
                recommendationsPerPage = Optional.presentIfNotNull(recommendationCount)
            )
        )
        .fetchPolicy(FetchPolicy.CacheAndNetwork)
        .toFlow()
        .filter { it.exception == null }
        .asResult { Media(it.media!!, language) }
    }

    suspend fun fetchMediaGenres(isAdult: Boolean) = apolloClient
        .query(GenresQuery())
        .fetchPolicy(FetchPolicy.CacheFirst)
        .execute()
        .data
        ?.GenreCollection
        ?.filterNotNull()
        ?.filter { genre -> if (!isAdult) genre != HENTAI else true }
        .orEmpty()
}
