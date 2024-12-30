package com.imashnake.animite.api.mal.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeSearchResponse(
    @SerialName("data") val data: List<PagingData>,
    @SerialName("paging") val paging: Paging
)

@Serializable
data class PagingData(
    @SerialName("node") val node: Node
)

@Serializable
data class Paging(
    @SerialName("next") val next: String?
)

@Serializable
data class AlternativeTitles(
    @SerialName("synonyms") val synonyms: List<String>,
    @SerialName("en") val en: String?,
    @SerialName("ja") val ja: String?
)

@Serializable
data class Broadcast(
    @SerialName("day_of_the_week") val dayOfTheWeek: String?,
    @SerialName("start_time") val startTime: String?
)

@Serializable
data class Genres(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?
)

@Serializable
data class MainPicture(
    @SerialName("medium") val medium: String?,
    @SerialName("large") val large: String?
)

@Serializable
data class Node(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String?,
    @SerialName("main_picture") val mainPicture: MainPicture?,
    @SerialName("alternative_titles") val alternativeTitles: AlternativeTitles?,
    @SerialName("start_date") val startDate: String?,
    @SerialName("end_date") val endDate: String?,
    @SerialName("synopsis") val synopsis: String?,
    @SerialName("mean") val mean: Double?,
    @SerialName("rank") val rank: Int?,
    @SerialName("popularity") val popularity: Int?,
    @SerialName("num_list_users") val numListUsers: Int?,
    @SerialName("num_scoring_users") val numScoringUsers: Int?,
    @SerialName("nsfw") val nsfw: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("media_type") val mediaType: String?,
    @SerialName("status") val status: String?,
    @SerialName("genres") val genres: List<Genres>,
    @SerialName("num_episodes") val numEpisodes: Int?,
    @SerialName("start_season") val startSeason: StartSeason?,
    @SerialName("broadcast") val broadcast: Broadcast?,
    @SerialName("source") val source: String?,
    @SerialName("average_episode_duration") val averageEpisodeDuration: Int?,
    @SerialName("rating") val rating: String?,
    @SerialName("studios") val studios: List<PagingStudios>
)

@Serializable
data class StartSeason(
    @SerialName("year") val year: Int?,
    @SerialName("season") val season: String?
)

@Serializable
data class PagingStudios(
    @SerialName("id") val id: Int?,
    @SerialName("name") val name: String?
)