package com.imashnake.animite.core.extensions

import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun LocalDateTime.toDayPart() = when (hour) {
    in 6..12 -> DayPart.MORNING
    in 12..17 -> DayPart.AFTERNOON
    in 17..20 -> DayPart.EVENING
    else -> DayPart.NIGHT
}

enum class DayPart {
    MORNING,
    AFTERNOON,
    EVENING,
    NIGHT,
}
