package com.imashnake.animite.features.navigationbar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
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
import com.imashnake.animite.R

enum class NavigationBarPaths(
    val navigateTo: (NavController) -> Unit,
    val matchesDestination: (NavBackStackEntry) -> Boolean,
    val icon: @Composable () -> Unit,
    @StringRes val labelRes: Int
) {
    RSlash(
        navigateTo = {
            it.navigate(com.imashnake.animite.rslash.RSlash) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    inclusive = true
                    saveState = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = {
            it.destination.hierarchy.any { it.hasRoute(com.imashnake.animite.rslash.RSlash::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.rslash), contentDescription = stringResource(R.string.rslash))
        },
        labelRes = R.string.home
    ),
    Home(
        navigateTo = {
            it.navigate(com.imashnake.animite.features.home.Home) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = {
            it.destination.hierarchy.any { it.hasRoute(com.imashnake.animite.features.home.Home::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.home), contentDescription = stringResource(R.string.home))
        },
        labelRes = R.string.home
    ),

    Profile(
        navigateTo = {
            it.navigate(com.imashnake.animite.profile.Profile()) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = {
            it.destination.hierarchy.any { it.hasRoute(com.imashnake.animite.profile.Profile::class) }
        },
        icon = {
            Icon(Icons.Rounded.AccountCircle, contentDescription = stringResource(R.string.profile))
        },
        labelRes = R.string.profile
    ),
}