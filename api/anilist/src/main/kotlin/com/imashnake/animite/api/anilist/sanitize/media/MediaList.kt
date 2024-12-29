package com.imashnake.animite.api.anilist.sanitize.media

data class MediaList(
    val type: Type,
    val list: List<Media.Small>
) {
    enum class Type(val title: String? = null) {
        TRENDING_NOW("Trending Now"),
        POPULAR_THIS_SEASON("Popular This Season"),
        UPCOMING_NEXT_SEASON("Upcoming Next Season"),
        ALL_TIME_POPULAR("All Time Popular"),
        SEARCH,
    }
}
