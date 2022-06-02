package com.imashnake.animite.data.sauce.apis.apollo

import com.apollographql.apollo3.api.Optional
import com.imashnake.animite.PopularThisSeasonQuery
import com.imashnake.animite.TrendingNowQuery
import com.imashnake.animite.data.sauce.apis.MediaListApi
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

/**
 * TODO: Kdoc.
 *
 * Example:
 *
 * **Anime:** Sono Bisque Doll wa Koi wo Suru;
 * **ID:** 132405.
 */
class ApolloMediaListApi @Inject constructor() : MediaListApi {
    override suspend fun fetchTrendingNowList(type: MediaType): TrendingNowQuery.Page? {
        return client
            .query(
                 TrendingNowQuery(
                     type = Optional.presentIfNotNull(type),
                     page = Optional.presentIfNotNull(0),
                     perPage = Optional.presentIfNotNull(10)
                 )
            )
            .execute().data?.page
    }

    override suspend fun fetchPopularThisSeasonList(
        type: MediaType,
        season: MediaSeason,
        seasonYear: Int
    ): PopularThisSeasonQuery.Page? {
        return client
            .query(
                PopularThisSeasonQuery(
                    type = Optional.presentIfNotNull(type),
                    page = Optional.presentIfNotNull(0),
                    perPage = Optional.presentIfNotNull(10),
                    season = Optional.presentIfNotNull(season),
                    seasonYear = Optional.presentIfNotNull(seasonYear)
                )
            )
            .execute().data?.page
    }
}
