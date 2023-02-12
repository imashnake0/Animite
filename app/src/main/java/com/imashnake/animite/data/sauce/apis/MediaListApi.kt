package com.imashnake.animite.data.sauce.apis

import com.apollographql.apollo3.api.ApolloResponse
import com.imashnake.animite.api.anilist.MediaListQuery
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaListApi {

    fun fetchMediaList(
        type: MediaType,
        page: Int,
        perPage: Int,
        sort: List<MediaSort>,
        season: MediaSeason?,
        seasonYear: Int?
    ): Flow<ApolloResponse<MediaListQuery.Data>>

    // And potentially other functions that help sort/modify the list.
}
