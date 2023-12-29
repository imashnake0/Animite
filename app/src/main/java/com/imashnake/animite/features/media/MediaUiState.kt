package com.imashnake.animite.features.media

import androidx.compose.ui.text.AnnotatedString
import com.imashnake.animite.api.anilist.sanitize.media.Media

data class MediaUiState(
    val bannerImage: String? = null,
    val coverImage: String? = null,
    val color: Int? = null,
    val title: String? = null,
    val description: AnnotatedString? = null,
    val ranks: List<Media.Ranking>? = null,
    val genres: List<String>? = null,
    val characters: List<Media.Character>? = null,
    val trailer: Media.Trailer? = null
)
