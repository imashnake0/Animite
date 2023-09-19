package com.imashnake.animite.data

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.route
import kotlinx.serialization.Serializable

sealed class AnimiteRoute : NavRoute {
    object Home : AnimiteRoute()

    @Serializable
    data class MediaPage(
        val id: Int,
        val mediaType: String
    ) : AnimiteRoute()

    object Profile : AnimiteRoute()

    object RSlash : AnimiteRoute()
}

val bottomNavRoutes = listOf(
    AnimiteRoute.Home.route,
    AnimiteRoute.Profile.route,
    AnimiteRoute.RSlash.route,
)
