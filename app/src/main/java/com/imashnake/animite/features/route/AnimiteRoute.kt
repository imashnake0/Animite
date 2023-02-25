package com.imashnake.animite.features.route

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.route
import kotlinx.serialization.Serializable

sealed interface AnimiteRoute : NavRoute {
    object Home : AnimiteRoute

    @Serializable
    data class MediaPage(
        val id: Int,
        val mediaType: String
    ) : AnimiteRoute

    object Profile : AnimiteRoute

    object RSlash : AnimiteRoute
}

val navBarVisibleRoutes = listOf(
    AnimiteRoute.Home.route,
    AnimiteRoute.Profile.route,
    AnimiteRoute.RSlash.route,
)
