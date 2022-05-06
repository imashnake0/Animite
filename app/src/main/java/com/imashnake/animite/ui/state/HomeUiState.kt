package com.imashnake.animite.ui.state

import com.imashnake.animite.AnimeQuery
import com.imashnake.animite.PopularThisSeasonQuery
import com.imashnake.animite.TrendingNowQuery

/**
 * TODO: Kdoc.
 */
data class HomeUiState(
    val animeList: List<AnimeQuery.Media?> = listOf(),
    val trendingAnimeList: TrendingNowQuery.Page? = null,
    val popularAnimeThisSeasonList: PopularThisSeasonQuery.Page? = null
)
