package com.imashnake.animite.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

enum class NavigationBarPaths(
    val navigateTo: (NavController) -> Unit,
    val matchesDestination: (NavBackStackEntry) -> Boolean,
    val icon: @Composable (Boolean) -> Unit,
    @param:StringRes val labelRes: Int
) {
    Social(
        navigateTo = {
            it.navigate(SocialRoute) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = { navBackStackEntry ->
            navBackStackEntry.destination.hierarchy.any { it.hasRoute(SocialRoute::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.social), contentDescription = stringResource(R.string.social))
        },
        labelRes = R.string.social
    ),
    Anime(
        navigateTo = {
            it.navigate(AnimeRoute) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = { navBackStackEntry ->
            navBackStackEntry.destination.hierarchy.any { it.hasRoute(AnimeRoute::class) }
        },
        icon = {
            if (it) {
                val infiniteTransition = rememberInfiniteTransition()
                val angle by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(12000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart,
                    ),
                )
                Box {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.anime_inner),
                        contentDescription = stringResource(R.string.anime)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.anime_outer),
                        contentDescription = stringResource(R.string.anime),
                        modifier = Modifier.graphicsLayer { rotationZ = angle }
                    )
                }
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.anime),
                    contentDescription = stringResource(R.string.anime)
                )
            }
        },
        labelRes = R.string.anime
    ),
    Manga(
        navigateTo = {
            it.navigate(MangaRoute) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = { navBackStackEntry ->
            navBackStackEntry.destination.hierarchy.any { it.hasRoute(MangaRoute::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.manga), contentDescription = stringResource(R.string.manga))
        },
        labelRes = R.string.manga
    ),

    Profile(
        navigateTo = {
            it.navigate(ProfileRoute()) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = { navBackStackEntry ->
            navBackStackEntry.destination.hierarchy.any { it.hasRoute(ProfileRoute::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.profile), contentDescription = stringResource(R.string.profile))
        },
        labelRes = R.string.profile
    ),
}
