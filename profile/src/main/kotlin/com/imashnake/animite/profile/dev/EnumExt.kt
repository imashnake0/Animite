package com.imashnake.animite.profile.dev

import com.imashnake.animite.profile.ui.Names
import com.imashnake.animite.media.R as mediaR

val Names.res get() = when(this) {
    Names.COMPLETED -> mediaR.drawable.finished
    Names.PAUSED -> mediaR.drawable.hiatus
    Names.DROPPED -> mediaR.drawable.cancelled
    Names.REWATCHING -> mediaR.drawable.releasing
    Names.PLANNING -> mediaR.drawable.not_yet_released
}
