package com.imashnake.animite.data.sauce

import com.imashnake.animite.TrendingNowQuery
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
class MediaListNetworkSource @Inject constructor(
    private val mediaListApi: MediaListApi,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun fetchMediaList(mediaType: MediaType): TrendingNowQuery.Media? =
        withContext(dispatcher) {
            mediaListApi.fetchList(mediaType)
        }
}
