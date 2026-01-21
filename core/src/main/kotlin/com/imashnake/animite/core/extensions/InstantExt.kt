package com.imashnake.animite.core.extensions

import androidx.compose.ui.util.fastRoundToInt
import kotlin.math.floor
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Float.toDayPart() = when (floor(this).fastRoundToInt()) {
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
