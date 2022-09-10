package com.imashnake.animite.features.navigationbar

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.imashnake.animite.features.NavGraphs
import com.imashnake.animite.features.appCurrentDestinationAsState
import com.imashnake.animite.features.destinations.HomeDestination
import com.imashnake.animite.features.destinations.ProfileDestination
import com.imashnake.animite.features.destinations.RSlashDestination
import com.imashnake.animite.features.startAppDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.imashnake.animite.R as Res

// TODO: Ripple where?
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun NavigationBar(
    navController: NavController,
    modifier: Modifier,
    itemModifier: Modifier
) {
    // TODO: The way padding is handled is still a bit hacky.
    NavigationBar(modifier = modifier) {
        val currentDestination = navController.appCurrentDestinationAsState().value
            ?: NavGraphs.root.startAppDestination

        NavigationBarPaths.values().forEach { destination ->
            NavigationBarItem(
                modifier = itemModifier,
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.navigate(destination.direction) {
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

enum class NavigationBarPaths(
    val direction: DirectionDestinationSpec,
    val icon: @Composable () -> Unit
) {
    RSlash(
        RSlashDestination,
        {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = Res.drawable.rslash
                ),
                contentDescription = stringResource(
                    id = Res.string.rslash
                )
            )
        }
    ),

    @ExperimentalAnimationApi
    @ExperimentalMaterial3Api
    Home(
        HomeDestination,
        {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = Res.drawable.home
                ),
                contentDescription = stringResource(
                    id = Res.string.home
                )
            )
        }
    ),
    @ExperimentalAnimationApi
    @ExperimentalMaterial3Api
    Profile(
        ProfileDestination,
        {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = stringResource(
                    id = Res.string.profile
                ),
                // Adding this modifier lets us control the icon's size.
                modifier = Modifier
                    .padding(3.dp)
                    .size(18.dp)
            )
        }
    )
}
