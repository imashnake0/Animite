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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    navigateTo: (NavKey) -> Unit,
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
            Root.paths.forEach { destination ->
                val selected by remember(backStack) {
                    derivedStateOf {
                        backStack.lastOrNull { it is Root } == destination
                    }
                }
                NavigationRailItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navigateTo(destination)
                        }
                    },
                    icon = {
                        // TODO: fix
                        Icon(
                            ImageVector.vectorResource(R.drawable.social),
                            contentDescription = stringResource(R.string.social)
                        )
                    },
                    modifier = Modifier.width(dimensionResource(R.dimen.navigation_rail_width))
                )
            }
        }
    }
}
