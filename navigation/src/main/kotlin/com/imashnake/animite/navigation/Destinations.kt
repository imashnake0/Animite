package com.imashnake.animite.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface Root : NavKey {
    @Serializable
    data class ProfileRoute(
        @SerialName("accessToken")
        val accessToken: String? = null,
        @SerialName("tokenType")
        val tokenType: String? = null,
        @SerialName("expiresIn")
        val expiresIn: Int = -1
    ) : Root

    @Serializable
    data object SocialRoute : Root

    @Serializable
    data object AnimeRoute : Root

    @Serializable
    data object MangaRoute : Root

    companion object {
        val paths = listOf(SocialRoute, AnimeRoute, MangaRoute, ProfileRoute())
    }
}

sealed interface Nested : NavKey {
    @Serializable
    data class MediaRoute(
        val id: Int,
        val source: String,
        val mediaType: String,
        val title: String?,
    ) : Nested

    @Serializable
    data object SettingsRoute : Nested
}
