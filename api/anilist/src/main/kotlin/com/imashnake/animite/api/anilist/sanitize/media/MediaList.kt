package com.imashnake.animite.api.anilist.sanitize.media

import androidx.compose.runtime.Stable
import com.imashnake.animite.api.anilist.sanitize.explore.FilterStrategy
import kotlinx.collections.immutable.ImmutableList

@Stable
data class MediaList(
    val title: String,
    val list: ImmutableList<Media.Small>,
    val filterStrategy: FilterStrategy,
)
