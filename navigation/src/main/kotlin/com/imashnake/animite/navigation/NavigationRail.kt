package com.imashnake.animite.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.NavigationRail as M3NavigationRail

@Composable
fun NavigationRail(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    // TODO: This uses `background` as `containerColor`, I have no idea why.
    M3NavigationRail(
        modifier = modifier.width(
            dimensionResource(R.dimen.navigation_rail_width) + WindowInsets
                .displayCutout
                .asPaddingValues()
                .calculateLeftPadding(LayoutDirection.Ltr)
        ),
        windowInsets = WindowInsets.displayCutout.only(WindowInsetsSides.Left).add(WindowInsets.statusBars)
    ) {
        NavigationBarPaths.entries.forEach { destination ->
            val selected = remember(destination, currentBackStackEntry) {
                currentBackStackEntry?.let { destination.matchesDestination(it) } ?: false
            }
            NavigationRailItem(
                selected = selected,
                onClick = { if (!selected) destination.navigateTo(navController) },
                icon = destination.icon,
                modifier = Modifier.navigationBarsPadding(),
            )
        }
    }
}
