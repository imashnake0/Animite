package com.imashnake.animite.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = modifier.height(
            dimensionResource(R.dimen.navigation_bar_height) + WindowInsets
                .navigationBars
                .asPaddingValues()
                .calculateBottomPadding()
        ),
        // TODO: Remove this after adding the rail.
        windowInsets = if (LocalConfiguration.current.orientation
            == Configuration.ORIENTATION_LANDSCAPE
        ) { WindowInsets.displayCutout } else { WindowInsets(0.dp) }
    ) {
        NavigationBarPaths.entries.forEach { destination ->
            val selected = remember(destination, currentBackStackEntry) {
                currentBackStackEntry?.let { destination.matchesDestination(it) } ?: false
            }
            NavigationBarItem(
                selected = selected,
                onClick = { if (!selected) destination.navigateTo(navController) },
                icon = destination.icon,
                modifier = Modifier.navigationBarsPadding(),
            )
        }
    }
}
