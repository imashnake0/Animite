package com.imashnake.animite.media.ext

import com.imashnake.animite.api.anilist.type.MediaSeason
import java.time.Month

/**
 * TODO: Add a unit test.
 * This groups the [Month]s  in 4 [List]s (each with 3 [Month]s).
 * There is an offset of two [Month]s, i.e., we need [Month.MARCH]..[Month.FEBRUARY]:
 *
 * [
 *
 * [[MARCH, APRIL, MAY]],
 *
 * [[JUNE, JULY, AUGUST]],
 *
 * [[SEPTEMBER, OCTOBER, NOVEMBER]],
 *
 * [[DECEMBER, JANUARY, FEBRUARY]]
 *
 * ]
 */
val seasons = with(Month.entries) {
    drop(2) + take(2)
}.chunked(3)



/**
 * Gets the [Month]'s season.
 *
 * @see seasons
 */
val Month.season get() = when(this) {
    in seasons[0] -> MediaSeason.SPRING
    in seasons[1] -> MediaSeason.SUMMER
    in seasons[2] -> MediaSeason.FALL
    in seasons[3] -> MediaSeason.WINTER
    else -> MediaSeason.UNKNOWN__
}
