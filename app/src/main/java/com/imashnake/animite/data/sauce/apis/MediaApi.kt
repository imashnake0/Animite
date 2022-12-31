package com.imashnake.animite.data.sauce.apis

import com.apollographql.apollo3.api.ApolloResponse
import com.imashnake.animite.MediaQuery
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaApi {

    fun fetchMedia(id: Int?, mediaType: MediaType): Flow<ApolloResponse<MediaQuery.Data>>
}
