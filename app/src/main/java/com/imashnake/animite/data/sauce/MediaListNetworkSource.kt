package com.imashnake.animite.data.sauce

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.data.sauce.apis.MediaListApi
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

class MediaListNetworkSource @Inject constructor(
    private val mediaListApi: MediaListApi
) {
    suspend fun fetchMediaList(
        mediaType: MediaType,
        page: Int,
        perPage: Int,
        sort: List<MediaSort>,
        // Popular This Season.
        season: MediaSeason?,
        seasonYear: Int?
    ): MediaListQuery.Page? {
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
