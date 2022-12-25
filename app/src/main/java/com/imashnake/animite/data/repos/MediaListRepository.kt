package com.imashnake.animite.data.repos

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.data.Resource
import com.imashnake.animite.data.sauce.asState
import com.imashnake.animite.data.sauce.network.MediaListNetworkSource
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaListRepository @Inject constructor(
    private val mediaListNetworkSource: MediaListNetworkSource
) {

    fun getMediaList(
        mediaType: MediaType,
        sort: List<MediaSort>,
        page: Int = 0,
        perPage: Int = 10,
        season: MediaSeason? = null,
        seasonYear: Int? = null
    ): Flow<Resource<MediaListQuery.Page?>> {
        return mediaListNetworkSource.fetchMediaList(
            mediaType = mediaType,
            page = page,
            perPage = perPage,
            sort = sort,
            season = season,
            seasonYear = seasonYear
        ).asState()
    }
}
