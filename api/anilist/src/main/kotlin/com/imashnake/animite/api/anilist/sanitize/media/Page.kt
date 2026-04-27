package com.imashnake.animite.api.anilist.sanitize.media

import androidx.compose.runtime.Stable
import com.imashnake.animite.api.anilist.MediaMediumListQuery
import kotlinx.collections.immutable.ImmutableList

@Stable
data class Page<T>(
    val list: ImmutableList<T>,
    val info: Info?,
)

@Stable
data class Info(
    val currentPage: Int?,
    val lastPage: Int?,
    val hasNextPage: Boolean?,
) {
    constructor(pageInfo: MediaMediumListQuery.PageInfo) : this(
        currentPage = pageInfo.currentPage,
        lastPage = pageInfo.lastPage,
        hasNextPage = pageInfo.hasNextPage
    )
}
