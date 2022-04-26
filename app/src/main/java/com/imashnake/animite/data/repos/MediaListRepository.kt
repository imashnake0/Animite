package com.imashnake.animite.data.repos

import com.imashnake.animite.TrendingNowQuery.Media
import com.imashnake.animite.data.sauce.MediaListNetworkSource
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
class MediaListRepository @Inject constructor(
    private val mediaListNetworkSource: MediaListNetworkSource
) {
    suspend fun fetchMediaList(mediaType: MediaType): Media? =
        mediaListNetworkSource.fetchMediaList(mediaType)
}
