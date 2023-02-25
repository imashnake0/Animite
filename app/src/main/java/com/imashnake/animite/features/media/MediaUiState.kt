package com.imashnake.animite.features.media

import com.imashnake.animite.api.anilist.sanitize.media.Media

data class MediaUiState(
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

data class Stat(
    val label: StatLabel,
    val score: Int
)

enum class StatLabel(val value: String) {
    SCORE("SCORE"),
    RATING("RATING"),
    POPULARITY("POPULARITY"),
    UNKNOWN("UNKNOWN")
}

data class Character(
    val id: Int?,
    val image: String?,
    val name: String?
)

data class Trailer(
    val link: String,
    val thumbnail: String
)
