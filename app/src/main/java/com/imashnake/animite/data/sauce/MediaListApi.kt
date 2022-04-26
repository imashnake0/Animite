package com.imashnake.animite.data.sauce

import com.imashnake.animite.TrendingNowQuery
import com.imashnake.animite.type.MediaType

/**
 * TODO:
 *  - Kdoc.
 *  - Pagination.
 */
interface MediaListApi {
    suspend fun fetchList(type: MediaType): TrendingNowQuery.Media?

    // And potentially other functions that help sort/modify the list.
}
