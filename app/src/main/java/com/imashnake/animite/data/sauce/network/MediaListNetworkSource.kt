package com.imashnake.animite.data.sauce.network

import com.apollographql.apollo3.exception.ApolloHttpException
import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.data.sauce.apis.MediaListApi
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.jvm.Throws

class MediaListNetworkSource @Inject constructor(
    private val mediaListApi: MediaListApi
) {
    @Throws(ApolloHttpException::class)
    fun fetchMediaList(
        mediaType: MediaType,
        page: Int,
        perPage: Int,
        sort: List<MediaSort>,
        // Popular This Season.
        season: MediaSeason?,
        seasonYear: Int?
    ): Flow<MediaListQuery.Page?> {
        return mediaListApi.fetchMediaList(
            type = mediaType,
            page = page,
            perPage = perPage,
            sort = sort,
            season = season,
            seasonYear = seasonYear
        )
    }
}
