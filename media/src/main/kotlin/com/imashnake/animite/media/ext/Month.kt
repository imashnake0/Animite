package com.imashnake.animite.media.ext

import com.imashnake.animite.api.anilist.type.MediaSeason
import kotlinx.datetime.Month

/**
 * Gets the [Month]'s season.
 */
val Month.season get() = when(this) {
    Month.JANUARY -> MediaSeason.WINTER
    Month.FEBRUARY -> MediaSeason.WINTER
    Month.MARCH -> MediaSeason.WINTER
    Month.APRIL -> MediaSeason.SPRING
    Month.MAY -> MediaSeason.SPRING
    Month.JUNE -> MediaSeason.SUMMER
    Month.JULY -> MediaSeason.SUMMER
    Month.AUGUST -> MediaSeason.SUMMER
    Month.SEPTEMBER -> MediaSeason.FALL
    Month.OCTOBER -> MediaSeason.FALL
    Month.NOVEMBER -> MediaSeason.FALL
    Month.DECEMBER -> MediaSeason.WINTER
}
