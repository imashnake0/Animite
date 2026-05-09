package com.imashnake.animite.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRoute(
    @SerialName("accessToken")
    val accessToken: String? = null,
    @SerialName("tokenType")
    val tokenType: String? = null,
    @SerialName("expiresIn")
    val expiresIn: Int = -1
)

@Serializable
data object SocialRoute

@Serializable
data class ExploreRoute(
    val mediaType: String? = null,
    val sortName: String? = null,
    val isDescending: Boolean? = null,
    val season: String? = null,
    val year: Int? = null,
    val genre: String? = null,
)

@Serializable
data object AnimeRoute

@Serializable
data object MangaRoute
