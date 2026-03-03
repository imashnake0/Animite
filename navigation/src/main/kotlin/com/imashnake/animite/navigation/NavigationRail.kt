package com.imashnake.animite.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey

@Composable
fun NavigationRail(
    backStack: List<NavKey>,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationRailDefaults.windowInsets.union(WindowInsets.displayCutout),
) {

    val insetPaddingValues = windowInsets.asPaddingValues()
    val layoutDirection = LocalLayoutDirection.current

    // This is a clone of Material3 NavigationRail
    Surface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = insetPaddingValues.calculateStartPadding(layoutDirection))
                .windowInsetsPadding(windowInsets.only(WindowInsetsSides.Vertical))
                .defaultMinSize(minWidth = dimensionResource(R.dimen.navigation_rail_width))
                .padding(vertical = 4.dp)
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NavigationBarPaths.entries.forEach { destination ->
                NavigationRailItem(
                    selected = backStack.contains(destination.route),
                    onClick = { onNavigate(destination.route) },
                    icon = {
                        Icon(
                            ImageVector.vectorResource(destination.icon),
                            contentDescription = stringResource(destination.iconDescription)
                        )
                    },
                    modifier = Modifier.width(dimensionResource(R.dimen.navigation_rail_width))
                )
            }
        }
    }
}
