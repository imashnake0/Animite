package com.imashnake.animite.navigation

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationBar(
    navController: NavController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar(
        Modifier.height(dimensionResource(R.dimen.navigation_bar_height)),
    ) {
        NavigationBarPaths.entries.forEach { destination ->
            val selected = remember(destination, currentBackStackEntry) {
                currentBackStackEntry?.let { destination.matchesDestination(it) } ?: false
            }
            NavigationBarItem(
                modifier = Modifier.navigationBarsPadding(),
                selected = selected,
                onClick = {
                    if (!selected) destination.navigateTo(navController)
                },
                icon = destination.icon
            )
        }
    }
}

enum class NavigationBarPaths(
    val navigateTo: (NavController) -> Unit,
    val matchesDestination: (NavBackStackEntry) -> Boolean,
    val icon: @Composable () -> Unit,
    @StringRes val labelRes: Int
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
        matchesDestination = {
            it.destination.hierarchy.any { it.hasRoute(SocialRoute::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.social), contentDescription = stringResource(R.string.social))
        },
        labelRes = R.string.social
    ),
    Home(
        navigateTo = {
            it.navigate(HomeRoute) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
        matchesDestination = {
            it.destination.hierarchy.any { it.hasRoute(HomeRoute::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.home), contentDescription = stringResource(R.string.home))
        },
        labelRes = R.string.home
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
        matchesDestination = {
            it.destination.hierarchy.any { it.hasRoute(ProfileRoute::class) }
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.profile), contentDescription = stringResource(R.string.profile))
        },
        labelRes = R.string.profile
    ),
}