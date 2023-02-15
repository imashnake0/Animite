package com.imashnake.animite.features.navigationbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.imashnake.animite.R
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination

@Composable
fun NavigationRail(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // TODO: This uses `background` as `containerColor`, I have no idea why.
    NavigationRail(
        modifier = modifier.width(
            dimensionResource(R.dimen.navigation_rail_width) + WindowInsets
                .displayCutout
                .asPaddingValues()
                .calculateLeftPadding(LayoutDirection.Ltr)
        ),
        windowInsets = WindowInsets.displayCutout.only(WindowInsetsSides.Left).add(WindowInsets.statusBars)
    ) {
        val currentDestination by navController.currentDestinationAsState()

        NavigationBarPaths.values().forEach { destination ->
            NavigationRailItem(
                selected = currentDestination?.startDestination == destination.route,
                onClick = {
                    navController.navigate(destination.route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = destination.icon
            )
        }
    }
}