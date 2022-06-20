package com.imashnake.animite.data.repos

import com.imashnake.animite.SearchQuery
import com.imashnake.animite.data.sauce.SearchNetworkSource
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchNetworkSource: SearchNetworkSource
) {
    suspend fun search(
        mediaType: MediaType,
        perPage: Int,
        search: String
    ): SearchQuery.Page? =
        searchNetworkSource.search(
            mediaType = mediaType,
            perPage = perPage,
            search = search
        )
}
