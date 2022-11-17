package com.imashnake.animite.data.repos

import com.imashnake.animite.MediaQuery
import com.imashnake.animite.data.sauce.MediaNetworkSource
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.delay
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaNetworkSource: MediaNetworkSource
) {
    suspend fun fetchMedia(id: Int?, mediaType: MediaType): MediaQuery.Media? {
        // TODO: Is there a better way to "wait for animations to complete"?
        //  searchBarBottomPadding's animateDpAsState has a finishedListener, how do I propagate
        //  this to `Home` so I can use it to navigate to `MediaPage` when true?
        delay(250)
        return mediaNetworkSource.fetchMedia(id, mediaType)
    }
}
