package com.imashnake.animite.api.anilist.sanitize.profile

import com.imashnake.animite.api.anilist.ViewerQuery

/** Sanitized [Viewer] */
data class Viewer(
    /** @see ViewerQuery.Viewer.id */
    val id: Int,
    /** @see ViewerQuery.Viewer.name */
    val name: String,
    /** @see ViewerQuery.Viewer.about */
    val about: String?,
    /** @see ViewerQuery.Viewer.avatar */
    val avatar: String?,
    /** @see ViewerQuery.Viewer.bannerImage */
    val banner: String?,
    /** @see ViewerQuery.Viewer.statistics */
    val genres: List<Genre>
) {
    /** @see ViewerQuery.Genre */
    data class Genre(
        /** @see ViewerQuery.Genre.genre */
        val genre: String,
        /** @see ViewerQuery.Genre.count */
        val mediaCount: Int,
    )

    internal constructor(query: ViewerQuery.Viewer) : this(
        id = query.id,
        name = query.name,
        about = query.about,
        avatar = query.avatar?.large,
        banner = query.bannerImage,
        genres = query.statistics?.anime?.genres.orEmpty().filterNotNull().run {
            val totalCount = this.sumOf { genre -> genre.count }
            mapNotNull {
                Genre(
                    genre = it.genre ?: return@mapNotNull null,
                    mediaCount = it.count
                )
            }.filter {
                // Filters out anime genres that contribute to less than 5%.
                it.mediaCount > totalCount/20
            }.sortedByDescending { it.mediaCount }
        }
    )
}
