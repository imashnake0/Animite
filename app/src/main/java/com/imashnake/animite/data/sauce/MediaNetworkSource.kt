package com.imashnake.animite.data.sauce

import com.imashnake.animite.MediaQuery.Media
import com.imashnake.animite.data.sauce.apis.MediaApi
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaNetworkSource @Inject constructor(
    private val mediaApi: MediaApi,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun fetchMedia(id: Int?, mediaType: MediaType): Media? =
        withContext(dispatcher) {
            mediaApi.fetchMedia(id, mediaType)
        }
}
