package com.imashnake.animite.ui.state

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.MediaQuery

/**
 * TODO: Kdoc.
 */
data class HomeUiState(
    val mediaList: List<MediaQuery.Media?> = listOf(),
    val trendingMediaList: MediaListQuery.Page? = null,
    val popularMediaThisSeasonList: MediaListQuery.Page? = null
)
