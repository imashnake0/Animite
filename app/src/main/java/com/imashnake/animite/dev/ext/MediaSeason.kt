package com.imashnake.animite.dev.ext

import com.imashnake.animite.type.MediaSeason
import java.time.LocalDate
import java.time.Month

/**
 * Returns the season after [this] season based on the current time.
 *
 * @param now The current time.
 * @return A [Pair], [Pair.first] is the next season and [Pair.second] season's year.
 */
fun MediaSeason.nextSeason(now: LocalDate): Pair<MediaSeason, Int> {
    return when (this) {
        MediaSeason.SPRING -> MediaSeason.SUMMER to now.year
        MediaSeason.SUMMER -> MediaSeason.FALL to now.year
        MediaSeason.FALL -> MediaSeason.WINTER to now.year + 1
        MediaSeason.WINTER -> MediaSeason.SPRING to if (now.month == Month.DECEMBER) {
            now.year + 1
        } else {
            now.year
        }
        else -> MediaSeason.UNKNOWN__ to 0
    }
}

val MediaSeason?.string
    get() = this?.rawValue?.lowercase()?.replaceFirstChar { it.uppercase() }
