package com.imashnake.animite.data.sauce.apis

import com.imashnake.animite.PopularThisSeasonQuery
import com.imashnake.animite.TrendingNowQuery
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaType

/**
 * TODO:
 *  - Kdoc.
 *  - Pagination.
 */
interface MediaListApi {
    suspend fun fetchTrendingNowList(type: MediaType): TrendingNowQuery.Page?

    suspend fun fetchPopularThisSeasonList(
        type: MediaType,
        season: MediaSeason,
        seasonYear: Int
    ): PopularThisSeasonQuery.Page?

    // And potentially other functions that help sort/modify the list.
}
