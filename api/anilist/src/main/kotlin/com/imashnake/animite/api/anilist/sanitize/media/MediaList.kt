package com.imashnake.animite.api.anilist.sanitize.media

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
data class MediaList(
    val type: Type,
    val list: ImmutableList<Media.Small>,
    val season: Media.Season? = null,
    val year: Int? = null,
) {
    enum class Type(val title: String? = null) {
        TRENDING_NOW("Trending Now"),
        POPULAR_THIS_SEASON("Popular This Season"),
        UPCOMING_NEXT_SEASON("Upcoming Next Season"),
        ALL_TIME_POPULAR("All Time Popular"),
        SEARCH,
    }
}
