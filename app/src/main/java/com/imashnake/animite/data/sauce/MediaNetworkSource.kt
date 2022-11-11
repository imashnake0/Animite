package com.imashnake.animite.data.sauce

import com.imashnake.animite.MediaQuery.Media
import com.imashnake.animite.data.sauce.apis.MediaApi
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

class MediaNetworkSource @Inject constructor(
    private val mediaApi: MediaApi
) {
    suspend fun fetchMedia(id: Int?, mediaType: MediaType): Media? {
        return mediaApi.fetchMedia(id, mediaType)
    }
}
