package com.imashnake.animite.media

import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.Media.Ranking
import kotlinx.collections.immutable.ImmutableList
import kotlin.time.Instant

data class MediaUiState(
    val source: String? = null,
    val id: Int? = null,
    val type: String? = null,
    val bannerImage: String? = null,
    val coverImage: String? = null,
    val color: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val nextAiringAt: Instant? = null,
    val nextEpisode: Int? = null,
    val info: ImmutableList<Media.Info>? = null,
    val year: String? = null,
    val season: Media.Season? = null,
    val rankings: ImmutableList<Pair<Ranking.TimeSpan, ImmutableList<Ranking>>>? = null,
    val genres: ImmutableList<String>? = null,
    val genreTitleList: Pair<String, ImmutableList<Media.Medium>>? = null,
    val characters: ImmutableList<Media.Credit>? = null,
    val staff: ImmutableList<Media.Credit>? = null,
    val trailer: Media.Trailer? = null,
    val streamingEpisodes: ImmutableList<Media.Episode>? = null,
    val relations: ImmutableList<Pair<Media.Relation?, Media.Small>>? = null,
    val recommendations: ImmutableList<Media.Small>? = null
)
