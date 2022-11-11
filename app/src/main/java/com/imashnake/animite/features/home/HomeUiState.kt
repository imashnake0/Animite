package com.imashnake.animite.features.home

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.data.sauce.db.model.Medium

/*data class HomeUiState(
    val trendingList: MediaListQuery.Page? = null,
    val popularList: MediaListQuery.Page? = null,
    val upcomingList: MediaListQuery.Page? = null,
    val allTimePopularList: MediaListQuery.Page? = null
)*/

data class HomeUiState(
    val trendingList: List<Medium>? = null,
    val popularList: List<Medium>? = null,
    val upcomingList: List<Medium>? = null,
    val allTimePopularList: List<Medium>? = null,
)
