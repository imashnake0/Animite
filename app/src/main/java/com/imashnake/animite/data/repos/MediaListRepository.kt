package com.imashnake.animite.data.repos

import com.imashnake.animite.PopularThisSeasonQuery
import com.imashnake.animite.TrendingNowQuery
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
    suspend fun fetchTrendingNowMediaList(mediaType: MediaType): TrendingNowQuery.Page? =
        mediaListNetworkSource.fetchTrendingNowMediaList(mediaType)

    suspend fun fetchPopularThisSeasonMediaList(
        mediaType: MediaType,
        season: MediaSeason,
        seasonYear: Int
    ): PopularThisSeasonQuery.Page? =
        mediaListNetworkSource.fetchPopularThisSeasonMediaList(mediaType, season, seasonYear)
}
