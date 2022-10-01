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
        MediaSeason.SPRING -> Pair(MediaSeason.SUMMER, now.year)
        MediaSeason.SUMMER -> Pair(MediaSeason.FALL, now.year)
        MediaSeason.FALL -> Pair(MediaSeason.WINTER, now.year + 1)
        MediaSeason.WINTER -> Pair(
            MediaSeason.SPRING,
            if (now.month == Month.DECEMBER) now.year + 1 else now.year
        )
        else -> Pair(MediaSeason.UNKNOWN__, 0)
    }
}
