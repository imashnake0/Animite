package com.imashnake.animite.data.repos

import com.imashnake.animite.MediaQuery
import com.imashnake.animite.data.Resource
import com.imashnake.animite.data.Resource.Companion.asResource
import com.imashnake.animite.data.sauce.apis.MediaApi
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaApi: MediaApi
) {
    fun fetchMedia(id: Int?, mediaType: MediaType): Flow<Resource<MediaQuery.Media?>> {
        // TODO: Is there a better way to "wait for animations to complete"?
        return mediaApi.fetchMedia(id, mediaType)
            .onStart { delay(100) }
            .asResource { it.media }
    }
}
