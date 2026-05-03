package com.imashnake.animite.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.produceState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * Provides context about the current time of day.
 *
 * @property dayProgress The progress of the day expressed as a percentage, where `0.0f` is 00:00,
 * and `1.0f` is 23:59.
 * @property dayPart A simplified model of [dayProgress] where the day is divided into phases derived
 * from the day progress.
 */
@Stable
data class TimeContext(
    val dayProgress: Float,
    val dayPart: DayPart
) {
    companion object {
        val DEFAULT = TimeContext(dayProgress = 0.5f, dayPart = DayPart.AFTERNOON)
        /**
         * Calculates a [TimeContext] for the given clock and timezone.
         */
        fun calculateFromNow(
            clock: Clock = Clock.System,
            timeZone: TimeZone = TimeZone.currentSystemDefault()
        ): TimeContext {
            val now = clock.now().toLocalDateTime(timeZone)
            return calculateFromTime(now.time)
        }

        /**
         * Calculates a [TimeContext] for the given [LocalTime].
         */
        fun calculateFromTime(
            time: LocalTime
        ): TimeContext {
            val dayPart = time.hour.toDayPart()
            val dayProgress = time.toSecondOfDay().toFloat() / 1.days.inWholeSeconds.toFloat()
            return TimeContext(
                dayProgress = dayProgress,
                dayPart = dayPart,
            )
        }
    }
}

enum class DayPart {
    MORNING,
    AFTERNOON,
    EVENING,
    NIGHT,
}

/**
 * The local [TimeContext]. Defaults to [TimeContext.DEFAULT].
 */
val LocalTimeContext = compositionLocalOf {
    TimeContext.DEFAULT
}

/**
 * Produces a live-updating State of [TimeContext]. New values are produced every minute.
 */
@Composable
fun produceTimeContext(): State<TimeContext> = produceState(
    TimeContext.calculateFromNow(Clock.System, TimeZone.currentSystemDefault())
) {
    while (this.coroutineContext.isActive) {
        delay(1.minutes)
        value = TimeContext.calculateFromNow(Clock.System, TimeZone.currentSystemDefault())
    }
}

private fun Int.toDayPart() = when (this) {
    in 6..11 -> DayPart.MORNING
    in 12..17 -> DayPart.AFTERNOON
    in 18..20 -> DayPart.EVENING
    else -> DayPart.NIGHT
}
