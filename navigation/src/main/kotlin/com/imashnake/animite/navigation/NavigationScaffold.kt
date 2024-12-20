package com.imashnake.animite.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController

@Composable
fun NavigationScaffold(
    navController: NavHostController,
    selectedDestination: NavigationBarPaths,
    modifier: Modifier = Modifier,
    layoutType: NavigationSuiteType =
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
    content: @Composable () -> Unit
) {
    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteItems = {
            NavigationBarPaths.entries.forEach { destination ->
                val selected = destination == selectedDestination
                item(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(destination.route) {
                                popUpTo(HomeRoute)
                            }
                        }
                    },
                    icon = destination.icon
                )
            }
        },
        modifier = modifier,
        content = content
    )
}
