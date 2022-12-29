package com.imashnake.animite.data.repos

import com.imashnake.animite.SearchQuery
import com.imashnake.animite.data.sauce.apis.SearchApi
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchApi: SearchApi
) {
    suspend fun search(
        mediaType: MediaType,
        perPage: Int,
        search: String
    ): SearchQuery.Page? =
        searchApi.search(
            type = mediaType,
            perPage = perPage,
            search = search
        )
}
