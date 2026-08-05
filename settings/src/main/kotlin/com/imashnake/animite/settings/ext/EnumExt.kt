package com.imashnake.animite.settings.ext

import com.imashnake.animite.api.anilist.type.ScoreFormat

val ScoreFormat.title get() = when(this) {
    ScoreFormat.POINT_100 -> "100"
    ScoreFormat.POINT_10_DECIMAL -> "10.0"
    ScoreFormat.POINT_10 -> "10"
    ScoreFormat.POINT_5 -> "5"
    ScoreFormat.POINT_3 -> "3"
    ScoreFormat.UNKNOWN__ -> ""
}
