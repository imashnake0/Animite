package com.imashnake.animite.features.media

data class MediaUiState(
    val bannerImage: String? = null,
    val coverImage: String? = null,
    val color: String? = null,
    val title: String? = null,
    val description: String? = null,
    val stats: List<Stat> = emptyList(),
    val genres: List<String?>? = null,
    val characters: List<Character>? = null,
    val trailer: Trailer = Trailer("", "")
)

data class Stat(
    val label: StatLabel,
    val score: Int?
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
    val link: String?,
    val thumbnail: String?
)
