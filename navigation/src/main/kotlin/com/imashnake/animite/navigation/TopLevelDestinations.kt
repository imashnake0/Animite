package com.imashnake.animite.navigation

import androidx.navigation3.runtime.NavKey
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
) : NavKey

@Serializable
data object SocialRoute : NavKey

@Serializable
data object AnimeRoute : NavKey

@Serializable
data object MangaRoute : NavKey
