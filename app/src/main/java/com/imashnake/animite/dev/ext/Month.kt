package com.imashnake.animite.dev.ext

import com.imashnake.animite.type.MediaSeason
import java.time.Month

val seasons = with(Month.values()) {
    drop(2) + take(2)
}.chunked(3)

/**
 * Gets the [Month]'s season.
 */
val Month.season get() = when(this) {
    in seasons[0] -> MediaSeason.SPRING
    in seasons[1] -> MediaSeason.SUMMER
    in seasons[2] -> MediaSeason.FALL
    in seasons[3] -> MediaSeason.WINTER
    else -> MediaSeason.UNKNOWN__
}
