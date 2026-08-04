package com.imashnake.animite.profile.dev

import com.imashnake.animite.api.anilist.sanitize.profile.User.TrackingStatus
import com.imashnake.animite.profile.R
import com.imashnake.animite.media.R as mediaR
import com.imashnake.animite.navigation.R as navigationR

val TrackingStatus.res get() = when(this) {
    TrackingStatus.WATCHING -> navigationR.drawable.anime
    TrackingStatus.COMPLETED -> mediaR.drawable.finished
    TrackingStatus.PAUSED -> mediaR.drawable.hiatus
    TrackingStatus.DROPPED -> mediaR.drawable.cancelled
    TrackingStatus.REWATCHING -> R.drawable.rewatch_list
    TrackingStatus.PLAN_TO_WATCH -> mediaR.drawable.not_yet_released

    TrackingStatus.READING -> R.drawable.reading_list
    TrackingStatus.REREADING -> R.drawable.rewatch_list
    TrackingStatus.PLAN_TO_READ -> mediaR.drawable.not_yet_released

    TrackingStatus.PLANNING -> mediaR.drawable.not_yet_released

    TrackingStatus.CUSTOM_OR_UNKNOWN -> R.drawable.custom_list
}

val TrackingStatus.title get() = when(this) {
    TrackingStatus.WATCHING -> R.string.watching
    TrackingStatus.COMPLETED -> R.string.completed
    TrackingStatus.PAUSED -> R.string.paused
    TrackingStatus.DROPPED -> R.string.dropped
    TrackingStatus.REWATCHING -> R.string.rewatching
    TrackingStatus.PLAN_TO_WATCH -> R.string.plan_to_watch

    TrackingStatus.READING -> R.string.reading
    TrackingStatus.REREADING -> R.string.rereading
    TrackingStatus.PLAN_TO_READ -> R.string.plan_to_read

    TrackingStatus.PLANNING,
    TrackingStatus.CUSTOM_OR_UNKNOWN -> R.string.unknown
}
