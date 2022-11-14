package com.imashnake.animite.features.navigationbar

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
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
@Composable
fun NavigationBar(
    navController: NavController
) {
    // TODO: Can we use `navigationBarsPadding()` instead?
    NavigationBar(
        Modifier.height(
            dimensionResource(Res.dimen.navigation_bar_height) + WindowInsets
                .navigationBars
                .asPaddingValues()
                .calculateBottomPadding()
        ),
        // TODO: Use a `NavigationRail` instead.
        windowInsets = if (LocalConfiguration.current.orientation
            == Configuration.ORIENTATION_LANDSCAPE
        ) { WindowInsets.displayCutout } else { WindowInsets(0.dp) }
    ) {
        val currentDestination = navController.appCurrentDestinationAsState().value
            ?: NavGraphs.root.startAppDestination

        NavigationBarPaths.values().forEach { destination ->
            NavigationBarItem(
                modifier = Modifier.navigationBarsPadding(),
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
    Profile(
        ProfileDestination,
        {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = stringResource(
                    id = Res.string.profile
                ),
                // TODO: Adding this modifier lets us control the icon's size;
                //  see how this works and unhardcode the dimensions.
                modifier = Modifier
                    .padding(3.dp)
                    .size(18.dp)
            )
        }
    )
}
