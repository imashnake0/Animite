package com.imashnake.animite.data.sauce.apis

import com.imashnake.animite.MediaQuery.Media
import com.imashnake.animite.type.MediaType

interface MediaApi {
    suspend fun fetchMedia(id: Int?, mediaType: MediaType): Media?
}
