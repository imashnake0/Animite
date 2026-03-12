package com.imashnake.animite.media.ext

import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSeason.FALL
import com.imashnake.animite.api.anilist.type.MediaSeason.SPRING
import com.imashnake.animite.api.anilist.type.MediaSeason.SUMMER
import com.imashnake.animite.api.anilist.type.MediaSeason.UNKNOWN__
import com.imashnake.animite.api.anilist.type.MediaSeason.WINTER
import kotlinx.datetime.LocalDateTime

/**
 * Returns the season after [this] season.
 */
val MediaSeason.nextSeason get() = when (this) {
    SPRING -> SUMMER
    SUMMER -> FALL
    FALL -> WINTER
    WINTER -> SPRING
    else -> UNKNOWN__
}

/**
 * Returns the year of [this] season.
 */
fun MediaSeason.nextSeasonYear(now: LocalDateTime) = when (this) {
    SPRING,
    SUMMER,
    WINTER -> now.year
    FALL -> now.year + 1
    UNKNOWN__ -> 0
}
