package com.imashnake.animite.data.sauce.apis

import com.imashnake.animite.AnimeQuery.Media

/**
 * TODO: Kdoc.
 */
interface AnimeApi {
    suspend fun fetchAnime(id: Int): Media?
}
