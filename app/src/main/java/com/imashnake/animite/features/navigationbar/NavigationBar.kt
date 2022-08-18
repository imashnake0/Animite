package com.imashnake.animite.features.navigationbar

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.imashnake.animite.R
import com.imashnake.animite.dev.internal.Path
import com.imashnake.animite.features.theme.NavigationItem

@Composable
fun NavigationBar(
    navController: NavController,
    modifier: Modifier
) {
    val paths = listOf(
        Path.RSlash,
        Path.Home,
        Path.Profile
    )

    // TODO: The way padding is handled is still a bit hacky.
    androidx.compose.material3.NavigationBar(
        containerColor = com.imashnake.animite.features.theme.NavigationBar,
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        paths.forEachIndexed { index, item ->
            NavigationBarItem(
                modifier = Modifier.navigationBarsPadding(),

                selected = currentDestination?.hierarchy?.any {
                    it.route == item.route
                } == true,

                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                icon = {
                    when (index) {
                        0 -> {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = R.drawable.rslash
                                ),
                                contentDescription = stringResource(
                                    id = item.stringRes
                                )
                            )
                        }
                        1 -> {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = R.drawable.home
                                ),
                                contentDescription = stringResource(
                                    id = item.stringRes
                                )
                            )
                        }
                        2 -> {
                            Icon(
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = stringResource(
                                    id = item.stringRes
                                ),
                                // Adding this modifier lets us control the icon's size.
                                modifier = Modifier
                                    .padding(3.dp)
                                    .size(18.dp)
                            )
                        }
                    }
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = com.imashnake.animite.features.theme.NavigationBar,
                    unselectedIconColor = NavigationItem,
                    indicatorColor = NavigationItem
                )
            )
        }
    }
}