package com.imashnake.animite.features.media

data class MediaUiState(
    val bannerImage: String? = "",
    val coverImage: String? = "",
    val color: String? = "",
    val title: String? = "",
    val description: String? = "",
    val stats: MutableList<Stat> = mutableListOf(),
    val genres: List<String?>? = listOf(),
    val characters: List<Pair<String?, String?>>? = listOf(),
    val trailer: Pair<String?, String?> = Pair("", "")
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
