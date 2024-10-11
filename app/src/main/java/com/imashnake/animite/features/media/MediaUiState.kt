package com.imashnake.animite.features.media

import com.imashnake.animite.api.anilist.sanitize.media.Media

data class MediaUiState(
    val source: String? = null,
    val id: Int? = null,
    val bannerImage: String? = null,
    val coverImage: String? = null,
    val color: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val ranks: List<Media.Ranking>? = null,
    val genres: List<String>? = null,
    val characters: List<Media.Character>? = null,
    val trailer: Media.Trailer? = null
)
