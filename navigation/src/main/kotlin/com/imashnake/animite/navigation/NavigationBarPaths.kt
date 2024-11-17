package com.imashnake.animite.navigation

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy

enum class NavigationBarPaths(
    val route: Any,
    val matchesDestination: (NavBackStackEntry) -> Boolean,
    val icon: @Composable () -> Unit,
    @StringRes val labelRes: Int
) {
    Social(
        route = SocialRoute,
        matchesDestination = {
            it.destination.hierarchy.any { it.hasRoute(SocialRoute::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.social), contentDescription = stringResource(R.string.social))
        },
        labelRes = R.string.social
    ),
    Home(
        route = HomeRoute,
        matchesDestination = {
            it.destination.hierarchy.any { it.hasRoute(HomeRoute::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.home), contentDescription = stringResource(R.string.home))
        },
        labelRes = R.string.home
    ),

    Profile(
        route = ProfileRoute(),
        matchesDestination = {
            it.destination.hierarchy.any { it.hasRoute(ProfileRoute::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.profile), contentDescription = stringResource(R.string.profile))
        },
        labelRes = R.string.profile
    ),
}