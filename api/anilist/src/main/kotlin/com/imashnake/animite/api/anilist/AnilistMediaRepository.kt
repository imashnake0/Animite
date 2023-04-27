package com.imashnake.animite.api.anilist

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.executeCacheAndNetwork
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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
            .executeCacheAndNetwork()
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
            .executeCacheAndNetwork()
            .asResult { Media(it.media!!) }
    }
}
