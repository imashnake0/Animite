package com.imashnake.animite.api.anilist.sanitize.explore

import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaFormat
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaStatus
import com.imashnake.animite.api.anilist.type.MediaType

data class FilterStrategy(
    val mediaType: MediaType,
    val sort: List<MediaSort>,
    val page: Int = 0,
    val perPage: Int = 10,
    val genre: String? = null,
    val includedGenres: List<String>? = null,
    val excludedGenres: List<String>? = null,
    val season: MediaSeason? = null,
    val year: Int? = null,
    val includedFormats: List<MediaFormat>? = null,
    val excludedFormats: List<MediaFormat>? = null,
    val includedStatuses: List<MediaStatus>? = null,
    val excludedStatuses: List<MediaStatus>? = null,
    val search: String? = null,
    val isAdult: Boolean = false,
    val isNsfwEnabled: Boolean = false,
    val language: Media.Language = Media.Language.DEFAULT,
)
