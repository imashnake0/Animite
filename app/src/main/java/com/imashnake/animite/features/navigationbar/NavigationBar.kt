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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.imashnake.animite.data.AnimiteRoute
import com.imashnake.animite.R as Res

// TODO: Ripple where?
@Composable
fun NavigationBar(
    currentRoute: String?,
    onNavigate: (AnimiteRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Can we use `navigationBarsPadding()` instead?
    NavigationBar(
        modifier.height(
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
        NavigationBarPaths.entries.forEach { destination ->
            NavigationBarItem(
                modifier = Modifier.navigationBarsPadding(),
                selected = currentRoute == destination.route.route,
                onClick = {
                    onNavigate(destination.route)
                },
                icon = destination.icon
            )
        }
    }
}

enum class NavigationBarPaths(
    val route: AnimiteRoute,
    val icon: @Composable () -> Unit
) {
    RSlash(
        route = AnimiteRoute.RSlash,
        icon = {
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
        route = AnimiteRoute.Home,
        icon = {
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
        route = AnimiteRoute.Profile,
        icon = {
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
