package com.imashnake.animite.core.extensions

import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Int.toDayPart() = when (this) {
    in 6..11 -> DayPart.MORNING
    in 12..17 -> DayPart.AFTERNOON
    in 18..20 -> DayPart.EVENING
    else -> DayPart.NIGHT
}

enum class DayPart {
    MORNING,
    AFTERNOON,
    EVENING,
    NIGHT,
}
