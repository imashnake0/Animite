package com.imashnake.animite.data.repos

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.data.sauce.MediaListNetworkSource
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
class MediaListRepository @Inject constructor(
    private val mediaListNetworkSource: MediaListNetworkSource
) {
    suspend fun fetchTrendingNowMediaList(
        mediaType: MediaType,
        page: Int,
        perPage: Int
    ): MediaListQuery.Page? =
        mediaListNetworkSource.fetchTrendingNowMediaList(
            mediaType = mediaType,
            page = page,
            perPage = perPage
        )

    suspend fun fetchPopularThisSeasonMediaList(
        mediaType: MediaType,
        page: Int,
        perPage: Int,
        season: MediaSeason,
        seasonYear: Int
    ): MediaListQuery.Page? =
        mediaListNetworkSource.fetchPopularThisSeasonMediaList(
            mediaType = mediaType,
            page = page,
            perPage = perPage,
            season = season,
            seasonYear = seasonYear
        )
}
