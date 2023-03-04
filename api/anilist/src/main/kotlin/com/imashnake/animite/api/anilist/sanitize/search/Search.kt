package com.imashnake.animite.api.anilist.sanitize.search

import com.imashnake.animite.api.anilist.SearchQuery

data class Search(
    /** @see SearchQuery.Medium.id */
    val id: Int,
    /** @see SearchQuery.Medium.coverImage */
    val coverImage: String?,
    /** @see SearchQuery.Medium.title */
    val title: String?,
    /** @see SearchQuery.Medium.season */
    val season: Season,
    /** @see SearchQuery.Medium.seasonYear */
    val seasonYear: Int?,
    /** @see SearchQuery.Medium.studios */
    val studios: List<String>,
    /** @see SearchQuery.Medium.format */
    val format: Format,
    /** @see SearchQuery.Medium.episodes */
    val episodes: Int?,
) {
    /** @see SearchQuery.Medium.season */
    enum class Season(val season: String) {
        WINTER("Winter"),
        SPRING("Spring"),
        SUMMER("Summer"),
        FALL("Fall"),
        UNKNOWN("")
    }

    /** @see SearchQuery.Medium.format */
    enum class Format(val format: String) {
        TV("TV"),
        TV_SHORT("TV SHORT"),
        MOVIE("MOVIE"),
        SPECIAL("SPECIAL"),
        OVA("OVA"),
        ONA("ONA"),
        MUSIC("MUSIC"),
        MANGA("MANGA"),
        NOVEL("NOVEL"),
        ONE_SHOT("ONE SHOT"),
        UNKNOWN("")
    }

    internal constructor(query: SearchQuery.Medium) : this(
        id = query.id,
        coverImage = query.coverImage?.extraLarge,
        title = query.title?.romaji ?: query.title?.english ?: query.title?.native,
        season = query.season?.let { Season.valueOf(it.name) } ?: Season.UNKNOWN,
        seasonYear = query.seasonYear,
        studios = if (query.studios?.nodes == null) { emptyList() } else {
            query.studios.nodes.filter { it?.name != null }.map { it!!.name }
        },
        format = query.format?.let { Format.valueOf(it.name) } ?: Format.UNKNOWN,
        episodes = query.episodes
    )
}
