package com.imashnake.animite.data.sauce

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.data.sauce.apis.MediaListApi
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
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
    suspend fun fetchTrendingNowMediaList(
        mediaType: MediaType,
        page: Int,
        perPage: Int
    ): MediaListQuery.Page? =
        withContext(dispatcher) {
            mediaListApi.fetchTrendingNowList(
                type = mediaType,
                page = page,
                perPage = perPage,
                sort = listOf(MediaSort.TRENDING_DESC)
            )
        }

    suspend fun fetchPopularThisSeasonMediaList(
        mediaType: MediaType,
        page:Int,
        perPage: Int,
        season: MediaSeason,
        seasonYear: Int
    ): MediaListQuery.Page? =
        withContext(dispatcher) {
            mediaListApi.fetchPopularThisSeasonList(
                type = mediaType,
                page = page,
                perPage = perPage,
                sort = listOf(MediaSort.POPULARITY_DESC),
                // TODO: These can be null!
                season = season,
                seasonYear = seasonYear
            )
        }
}
