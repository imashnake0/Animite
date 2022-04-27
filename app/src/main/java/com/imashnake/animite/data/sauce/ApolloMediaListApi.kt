package com.imashnake.animite.data.sauce

import com.apollographql.apollo3.api.Optional
import com.imashnake.animite.TrendingNowQuery
import com.imashnake.animite.TrendingNowQuery.Page
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
    override suspend fun fetchTrendingNowList(type: MediaType): Page? {
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
}
