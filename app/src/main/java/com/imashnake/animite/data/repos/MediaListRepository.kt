package com.imashnake.animite.data.repos

import com.imashnake.animite.api.anilist.MediaListQuery
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.data.Resource
import com.imashnake.animite.data.Resource.Companion.asResource
import com.imashnake.animite.data.sauce.apis.MediaListApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaListRepository @Inject constructor(
    private val mediaListApi: MediaListApi
) {
    fun fetchMediaList(
        mediaType: MediaType,
        sort: List<MediaSort>,
        page: Int = 0,
        perPage: Int = 10,
        season: MediaSeason? = null,
        seasonYear: Int? = null
    ): Flow<Resource<MediaListQuery.Page?>> {
        return mediaListApi.fetchMediaList(
            type = mediaType,
            page = page,
            perPage = perPage,
            sort = sort,
            season = season,
            seasonYear = seasonYear
        ).asResource { data -> data.page }
    }
}
