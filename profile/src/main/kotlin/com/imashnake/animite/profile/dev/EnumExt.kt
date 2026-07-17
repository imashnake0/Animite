package com.imashnake.animite.profile.dev

import com.imashnake.animite.api.anilist.sanitize.profile.User.ListNames
import com.imashnake.animite.profile.R
import com.imashnake.animite.media.R as mediaR
import com.imashnake.animite.navigation.R as navigationR

val ListNames.res get() = when(this) {
    ListNames.WATCHING -> navigationR.drawable.anime
    ListNames.COMPLETED -> mediaR.drawable.finished
    ListNames.PAUSED -> mediaR.drawable.hiatus
    ListNames.DROPPED -> mediaR.drawable.cancelled
    ListNames.REWATCHING -> R.drawable.rewatch_list
    ListNames.PLANNING -> mediaR.drawable.not_yet_released

    ListNames.READING -> R.drawable.reading_list
    ListNames.REREADING -> R.drawable.rewatch_list
    ListNames.PLAN_TO_READ -> mediaR.drawable.not_yet_released

    ListNames.CUSTOM_OR_UNKNOWN -> R.drawable.custom_list
}
