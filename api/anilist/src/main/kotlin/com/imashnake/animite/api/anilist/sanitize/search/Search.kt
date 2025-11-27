package com.imashnake.animite.api.anilist.sanitize.search

import com.imashnake.animite.api.anilist.fragment.MediaMedium

data class Search(
    /** @see id */
    val id: Int,
    /** @see coverImage */
    val coverImage: String?,
    /** @see title */
    val title: String?,
    /** @see season */
    val season: Season,
    /** @see seasonYear */
    val seasonYear: Int?,
    /** @see studios */
    val studios: List<String>,
    /** @see format */
    val format: Format,
    /** @see episodes */
    val episodes: Int?,
) {
    /** @see season */
    enum class Season(val season: String) {
        WINTER("Winter"),
        SPRING("Spring"),
        SUMMER("Summer"),
        FALL("Fall"),
        UNKNOWN("")
    }

    /** @see format */
    enum class Format(val string: String) {
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

    internal constructor(query: MediaMedium) : this(
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
