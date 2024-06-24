package com.imashnake.animite.features.navigationbar

import android.content.res.Configuration
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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.isRouteOnBackStack
import com.ramcosta.composedestinations.utils.startDestination
import com.imashnake.animite.R
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.rslash.destinations.RslashScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.UserDestination
import com.imashnake.animite.core.R as coreR

// TODO: Ripple where?
@Composable
fun NavigationBar(
    navController: NavController
) {
    // TODO: Can we use `navigationBarsPadding()` instead?
    NavigationBar(
        Modifier.height(
            dimensionResource(coreR.dimen.navigation_bar_height) + WindowInsets
                .navigationBars
                .asPaddingValues()
                .calculateBottomPadding()
        ),
        // TODO: Use a `NavigationRail` instead.
        windowInsets = if (LocalConfiguration.current.orientation
            == Configuration.ORIENTATION_LANDSCAPE
        ) { WindowInsets.displayCutout } else { WindowInsets(0.dp) }
    ) {
        val currentDestination by navController.currentDestinationAsState()
        NavigationBarPaths.entries.forEach { destination ->
            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(destination.route)
            NavigationBarItem(
                modifier = Modifier.navigationBarsPadding(),
                selected = currentDestination?.startDestination == destination.route,
                onClick = {
                    if (isCurrentDestOnBackStack) {
                        navController.popBackStack(destination.route.route, false)
                        return@NavigationBarItem
                    }

                    navController.navigate(destination.route.route) {
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
    val route: DestinationSpec,
    val icon: @Composable () -> Unit
) {
    RSlash(
        RslashScreenDestination,
        {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = R.drawable.rslash
                ),
                contentDescription = stringResource(
                    id = R.string.rslash
                )
            )
        }
    ),
    Home(
        HomeScreenDestination,
        {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = R.drawable.home
                ),
                contentDescription = stringResource(
                    id = R.string.home
                )
            )
        }
    ),
    Profile(
        UserDestination,
        {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = stringResource(
                    id = R.string.profile
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
