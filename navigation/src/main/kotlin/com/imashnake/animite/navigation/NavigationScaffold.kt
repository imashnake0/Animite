package com.imashnake.animite.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationScaffold(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    layoutType: NavigationSuiteType =
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
    content: @Composable () -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteItems = {
            NavigationBarPaths.entries.forEach { destination ->
                item(
                    selected = currentBackStackEntry?.let { destination.matchesDestination(it) } == true,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(HomeRoute)
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
