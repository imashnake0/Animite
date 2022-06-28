package com.imashnake.animite.data.sauce.apis

import com.imashnake.animite.SearchQuery
import com.imashnake.animite.type.MediaType

/**
 * TODO: Kdoc.
 */
interface SearchApi {
    suspend fun search(
        type: MediaType,
        perPage: Int,
        search: String
    ): SearchQuery.Page?
}
