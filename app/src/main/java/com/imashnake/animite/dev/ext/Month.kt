package com.imashnake.animite.dev.ext

import com.imashnake.animite.type.MediaSeason
import java.time.Month

/**
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
val seasons = with(Month.values()) {
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
