package com.imashnake.animite.data.repos

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.data.sauce.apis.MediaListApi
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

class MediaListRepository @Inject constructor(
    private val mediaListApi: MediaListApi
) {
    suspend fun fetchMediaList(
        mediaType: MediaType,
        page: Int,
        perPage: Int,
        sort: List<MediaSort>,
        season: MediaSeason?,
        seasonYear: Int?
    ): MediaListQuery.Page? =
        mediaListApi.fetchMediaList(
            type = mediaType,
            page = page,
            perPage = perPage,
            sort = sort,
            season = season,
            seasonYear = seasonYear
        )
}
