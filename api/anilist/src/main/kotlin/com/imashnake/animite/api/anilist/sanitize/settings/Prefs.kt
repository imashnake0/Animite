package com.imashnake.animite.api.anilist.sanitize.settings

import com.imashnake.animite.api.anilist.sanitize.media.Media

data class Prefs(
    val isNsfwEnabled: Boolean,
    val language: Media.Language,
)
