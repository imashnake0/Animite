package com.imashnake.animite.features.media

import com.imashnake.animite.type.MediaRankType

data class MediaUiState(
    val bannerImage: String? = "",
    val coverImage: String? = "",
    val title: String? = "",
    val description: String? = "",
    val averageScore: Int? = null,
    val ranks: MutableList<Pair<String, Int>> = mutableListOf(),
    val genres: List<String?>? = listOf(),
    val characters: List<Pair<String?, String?>>? = listOf(),
    val trailer: Pair<String?, String?> = Pair("", "")
)
