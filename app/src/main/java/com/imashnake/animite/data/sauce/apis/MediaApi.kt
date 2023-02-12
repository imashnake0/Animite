package com.imashnake.animite.data.sauce.apis

import com.apollographql.apollo3.api.ApolloResponse
import com.imashnake.animite.api.anilist.MediaQuery
import com.imashnake.animite.api.anilist.type.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaApi {

    fun fetchMedia(id: Int?, mediaType: MediaType): Flow<ApolloResponse<MediaQuery.Data>>
}
