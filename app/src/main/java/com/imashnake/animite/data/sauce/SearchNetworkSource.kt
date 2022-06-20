package com.imashnake.animite.data.sauce

import com.imashnake.animite.SearchQuery
import com.imashnake.animite.data.sauce.apis.SearchApi
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchNetworkSource @Inject constructor(
    private val searchApi: SearchApi,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun search(
        mediaType: MediaType,
        perPage: Int,
        search: String
    ): SearchQuery.Page? =
        withContext(dispatcher) {
            searchApi.search(
                type = mediaType,
                perPage = perPage,
                search = search
            )
        }
}
