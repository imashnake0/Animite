package com.imashnake.animite.media.ext

import com.imashnake.animite.api.anilist.type.MediaSeason
import kotlinx.datetime.Month

/**
 * Gets the [Month]'s season:
 * [Definition](https://anilist.notion.site/Release-Data-72ae49d8449842868b73520512b5471e).
 */
val Month.season get() = when(this) {
    Month.JANUARY -> MediaSeason.WINTER
    Month.FEBRUARY -> MediaSeason.WINTER
    Month.MARCH -> MediaSeason.WINTER
    Month.APRIL -> MediaSeason.SPRING
    Month.MAY -> MediaSeason.SPRING
    Month.JUNE -> MediaSeason.SPRING
    Month.JULY -> MediaSeason.SUMMER
    Month.AUGUST -> MediaSeason.SUMMER
    Month.SEPTEMBER -> MediaSeason.SUMMER
    Month.OCTOBER -> MediaSeason.FALL
    Month.NOVEMBER -> MediaSeason.FALL
    Month.DECEMBER -> MediaSeason.FALL
}
