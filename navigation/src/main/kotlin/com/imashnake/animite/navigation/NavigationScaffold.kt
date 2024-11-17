package com.imashnake.animite.navigation

import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationScaffold(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            NavigationBarPaths.entries.forEach { destination ->
                item(
                    selected = currentBackStackEntry?.let { destination.matchesDestination(it) } == true,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
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
