package com.imashnake.animite.profile.tabs

import androidx.annotation.StringRes
import com.imashnake.animite.profile.R

enum class ProfileTab(@param:StringRes val titleRes: Int) {
    ABOUT(R.string.about),
    ANIME(R.string.anime),
    MANGA(R.string.manga),
    FAVOURITES(R.string.favourites),
    STATISTICS(R.string.statistics)
}
