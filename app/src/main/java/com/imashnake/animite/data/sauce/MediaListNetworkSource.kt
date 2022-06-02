package com.imashnake.animite.data.sauce

import com.imashnake.animite.PopularThisSeasonQuery
import com.imashnake.animite.TrendingNowQuery
import com.imashnake.animite.data.sauce.apis.MediaListApi
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
class MediaListNetworkSource @Inject constructor(
    private val mediaListApi: MediaListApi,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun fetchTrendingNowMediaList(mediaType: MediaType): TrendingNowQuery.Page? =
        withContext(dispatcher) {
            mediaListApi.fetchTrendingNowList(mediaType)
        }

    suspend fun fetchPopularThisSeasonMediaList(
        mediaType: MediaType,
        season: MediaSeason,
        seasonYear: Int
    ): PopularThisSeasonQuery.Page? =
        withContext(dispatcher) {
            mediaListApi.fetchPopularThisSeasonList(mediaType, season, seasonYear)
        }
}
