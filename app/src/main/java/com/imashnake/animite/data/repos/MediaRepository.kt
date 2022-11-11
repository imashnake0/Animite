package com.imashnake.animite.data.repos

import com.imashnake.animite.MediaQuery
import com.imashnake.animite.data.sauce.network.MediaNetworkSource
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.delay
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaNetworkSource: MediaNetworkSource
) {
    suspend fun fetchMedia(id: Int?, mediaType: MediaType): MediaQuery.Media? {
        // TODO: Is there a better way to "wait for animations to complete"?
        delay(100)
        return mediaNetworkSource.fetchMedia(id, mediaType)
    }
}
