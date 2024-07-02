package com.imashnake.animite.api.anilist.sanitize.profile

import com.imashnake.animite.api.anilist.ViewerQuery
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

/**
 * Sanitized [ViewerQuery.Viewer].
 *
 * @param id
 * @param name
 * @param about
 * @param avatar
 * @param banner
 * @param count
 * @param daysWatched
 * @param meanScore
 * @param genres
 */
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
    // region About
    /** @see ViewerQuery.Anime.count */
    val count: Int?,
    /** @see ViewerQuery.Anime.minutesWatched */
    val daysWatched: Double?,
    /** @see ViewerQuery.Anime.meanScore */
    val meanScore: Float?,
    /** @see ViewerQuery.Anime.genres */
    val genres: List<Genre>
    // endregion
) {
    /**
     * Sanitized [ViewerQuery.Genre]
     *
     * @param genre
     * @param mediaCount
     */
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
        count = query.statistics?.anime?.count,
        daysWatched = query.statistics?.anime?.minutesWatched
            ?.minutes?.toDouble(DurationUnit.DAYS),
        meanScore = query.statistics?.anime?.meanScore?.toFloat(),
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
