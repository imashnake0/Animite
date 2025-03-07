package com.imashnake.animite.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationRail(
    navController: NavController,
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationRailDefaults.windowInsets.union(WindowInsets.displayCutout),
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    // This is a clone of Material3 NavigationRail, with a required width of 80dp
    Surface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        modifier = modifier,
    ) {
        Column(
             modifier = Modifier
                 .fillMaxHeight()
                 .windowInsetsPadding(windowInsets)
                 .defaultMinSize(minWidth = dimensionResource(R.dimen.navigation_rail_width))
                 .padding(vertical = 4.dp)
                 .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NavigationBarPaths.entries.forEach { destination ->
                val selected = remember(destination, currentBackStackEntry) {
                    currentBackStackEntry?.let { destination.matchesDestination(it) } == true
                }
                NavigationRailItem(
                    selected = selected,
                    onClick = { if (!selected) destination.navigateTo(navController) },
                    icon = destination.icon,
                    modifier = Modifier.width(dimensionResource(R.dimen.navigation_rail_width))
                )
            }
        }
    }
}
