package com.imashnake.animite.data.repos

import com.imashnake.animite.MediaQuery
import com.imashnake.animite.data.sauce.MediaNetworkSource
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
class MediaRepository @Inject constructor(
    private val mediaNetworkSource: MediaNetworkSource
) {
    suspend fun fetchMedia(id: Int?, mediaType: MediaType): MediaQuery.Media? =
        mediaNetworkSource.fetchMedia(id, mediaType)
}
