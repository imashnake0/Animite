package com.imashnake.animite.features.home

import com.imashnake.animite.MediaListQuery

data class HomeUiState(
    val trendingMediaList: MediaListQuery.Page? = null,
    val popularThisSeasonMediaList: MediaListQuery.Page? = null
)
