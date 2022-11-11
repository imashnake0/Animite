package com.imashnake.animite.data.sauce

import com.imashnake.animite.SearchQuery
import com.imashnake.animite.data.sauce.apis.SearchApi
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

class SearchNetworkSource @Inject constructor(
    private val searchApi: SearchApi
) {
    suspend fun search(
        mediaType: MediaType,
        perPage: Int,
        search: String
    ): SearchQuery.Page? {
        return searchApi.search(
            type = mediaType,
            perPage = perPage,
            search = search
        )
    }
}
