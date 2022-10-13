package com.imashnake.animite.features.home

import com.imashnake.animite.MediaListQuery

data class HomeUiState(
    val trendingList: MediaListQuery.Page? = null,
    val popularList: MediaListQuery.Page? = null,
    val upcomingList: MediaListQuery.Page? = null,
    val allTimePopularList: MediaListQuery.Page? = null
)
