package com.imashnake.animite.data.sauce.apis

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaListApi {
    fun fetchMediaList(
        type: MediaType,
        page: Int,
        perPage: Int,
        sort: List<MediaSort>,
        season: MediaSeason?,
        seasonYear: Int?
    ): Flow<MediaListQuery.Page?>

    // And potentially other functions that help sort/modify the list.
}
