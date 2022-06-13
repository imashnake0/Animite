package com.imashnake.animite.data.repos

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.data.sauce.MediaListNetworkSource
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
class MediaListRepository @Inject constructor(
    private val mediaListNetworkSource: MediaListNetworkSource
) {
    suspend fun fetchMediaList(
        mediaType: MediaType,
        page: Int,
        perPage: Int,
        sort: List<MediaSort>,
        season: MediaSeason?,
        seasonYear: Int?
    ): MediaListQuery.Page? =
        mediaListNetworkSource.fetchMediaList(
            mediaType = mediaType,
            page = page,
            perPage = perPage,
            sort = sort,
            season = season,
            seasonYear = seasonYear
        )
}
