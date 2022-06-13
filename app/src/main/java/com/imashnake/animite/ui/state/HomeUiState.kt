package com.imashnake.animite.ui.state

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.MediaQuery

/**
 * TODO: Kdoc.
 */
data class HomeUiState(
    val mediaList: List<MediaQuery.Media?> = listOf(),
    val trendingAnimeList: MediaListQuery.Page? = null,
    val popularAnimeThisSeasonList: MediaListQuery.Page? = null
)
