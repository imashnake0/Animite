package com.imashnake.animite.data.sauce

import com.imashnake.animite.AnimeQuery.Media

/**
 * TODO: Kdoc.
 */
interface AnimeApi {
    suspend fun fetchAnime(): Media?
}
