package com.imashnake.animite.data.repos

import com.imashnake.animite.api.anilist.SearchQuery
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.data.Resource
import com.imashnake.animite.data.Resource.Companion.asResource
import com.imashnake.animite.data.sauce.apis.SearchApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchApi: SearchApi
) {
    fun search(
        mediaType: MediaType,
        perPage: Int = 10,
        search: String? = null
    ): Flow<Resource<SearchQuery.Page?>> =
        searchApi.search(
            type = mediaType,
            perPage = perPage,
            search = search
        ).asResource { data -> search?.let { data.page }}
}
