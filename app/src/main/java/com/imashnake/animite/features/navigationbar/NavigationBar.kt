package com.imashnake.animite.features.navigationbar

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.imashnake.animite.R
import com.imashnake.animite.core.R as coreR

@Composable
fun NavigationBar(
    navController: NavController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    // TODO: Can we use `navigationBarsPadding()` instead?
    NavigationBar(
        Modifier.height(
            dimensionResource(coreR.dimen.navigation_bar_height) + WindowInsets
                .navigationBars
                .asPaddingValues()
                .calculateBottomPadding()
        ),
        // TODO: Use a `NavigationRail` instead.
        windowInsets = if (LocalConfiguration.current.orientation
            == Configuration.ORIENTATION_LANDSCAPE
        ) { WindowInsets.displayCutout } else { WindowInsets(0.dp) }
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
    RSlash(
        navigateTo = {
            it.navigate(com.imashnake.animite.rslash.RSlash) {
                popUpTo(id = it.graph.findStartDestination().id) {
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
            it.navigate(com.imashnake.animite.profile.Profile) {
                popUpTo(id = it.graph.findStartDestination().id) {
                    saveState = true
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