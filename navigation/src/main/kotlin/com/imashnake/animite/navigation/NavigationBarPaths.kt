package com.imashnake.animite.navigation

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation3.runtime.NavKey

enum class NavigationBarPaths(
    val navigateTo: (NavController) -> Unit,
    val matchesDestination: (NavBackStackEntry) -> Boolean,
    val icon: @Composable () -> Unit,
    @param:StringRes val labelRes: Int
) : NavKey {
    Social(
        navigateTo = {
            it.navigate(Root.SocialRoute) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = { navBackStackEntry ->
            navBackStackEntry.destination.hierarchy.any { it.hasRoute(Root.SocialRoute::class) }
        },
        icon = {
            Icon(
                ImageVector.vectorResource(R.drawable.social),
                contentDescription = stringResource(R.string.social)
            )
        },
        labelRes = R.string.social
    ),
    Anime(
        navigateTo = {
            it.navigate(Root.AnimeRoute) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = { navBackStackEntry ->
            navBackStackEntry.destination.hierarchy.any { it.hasRoute(Root.AnimeRoute::class) }
        },
        icon = {
            Icon(
                ImageVector.vectorResource(R.drawable.anime),
                contentDescription = stringResource(R.string.anime)
            )
        },
        labelRes = R.string.anime
    ),
    Manga(
        navigateTo = {
            it.navigate(Root.MangaRoute) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = { navBackStackEntry ->
            navBackStackEntry.destination.hierarchy.any { it.hasRoute(Root.MangaRoute::class) }
        },
        icon = {
            Icon(
                ImageVector.vectorResource(R.drawable.manga),
                contentDescription = stringResource(R.string.manga)
            )
        },
        labelRes = R.string.manga
    ),

    Profile(
        navigateTo = {
            it.navigate(Root.ProfileRoute()) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = { navBackStackEntry ->
            navBackStackEntry.destination.hierarchy.any { it.hasRoute(Root.ProfileRoute::class) }
        },
        icon = {
            Icon(
                ImageVector.vectorResource(R.drawable.profile),
                contentDescription = stringResource(R.string.profile)
            )
        },
        labelRes = R.string.profile
    ),
}
