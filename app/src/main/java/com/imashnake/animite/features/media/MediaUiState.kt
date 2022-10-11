package com.imashnake.animite.features.media

data class MediaUiState(
    val bannerImage: String? = "",
    val coverImage: String? = "",
    val color: String? = "",
    val title: String? = "",
    val description: String? = "",
    val stats: MutableList<Stat> = mutableListOf(),
    val genres: List<String?>? = emptyList(),
    val characters: List<Character>? = emptyList(),
    val trailer: Trailer = Trailer("", "")
)

data class Stat(
    val label: StatLabel,
    val score: Int?
)

enum class StatLabel(val value: String) {
    SCORE("SCORE"),
    RATING("RATING"),
    POPULARITY("POPULARITY")
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
