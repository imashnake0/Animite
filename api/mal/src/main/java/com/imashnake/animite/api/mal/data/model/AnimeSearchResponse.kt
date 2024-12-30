package com.imashnake.animite.api.mal.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeSearchResponse(
    @SerialName("data") val data: Data
)

@Serializable
data class Data(
    @SerialName("page") val page: Page
)

@Serializable
data class Page(
    @SerialName("media") val media: List<Media>
)

@Serializable
data class Media(
    @SerialName("id") val id: Int,
    @SerialName("coverImage") val coverImage: CoverImage?,
    @SerialName("title") val title: Title?,
    @SerialName("season") val season: String?,
    @SerialName("seasonYear") val seasonYear: Int?,
    @SerialName("studios") val studios: Studios?,
    @SerialName("format") val format: String?,
    @SerialName("episodes") val episodes: Int?
)

@Serializable
data class CoverImage(
    @SerialName("extraLarge") val extraLarge: String?
)

@Serializable
data class Title(
    @SerialName("romaji") val romaji: String?,
    @SerialName("english") val english: String?,
    @SerialName("native") val native: String?
)

@Serializable
data class Studios(
    @SerialName("nodes") val nodes: List<Nodes>
)

@Serializable
data class Nodes(
    @SerialName("name") val name: String?
)
