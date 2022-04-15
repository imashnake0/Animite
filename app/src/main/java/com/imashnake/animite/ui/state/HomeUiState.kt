package com.imashnake.animite.ui.state

import com.imashnake.animite.AnimeQuery

/**
 * TODO: Kdoc.
 */
data class HomeUiState(
    val animeList: List<AnimeQuery.Media?> = listOf()
)
