package com.imashnake.animite.features.navigationbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.imashnake.animite.R
import com.imashnake.animite.features.destinations.HomeScreenDestination
import com.imashnake.animite.profile.ProfileNavGraph
import com.imashnake.animite.rslash.RslashNavGraph
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.utils.startDestination

enum class NavigationBarPaths(
    val route: DestinationSpec<*>,
    val icon: @Composable () -> Unit
) {
    RSlash(
        RslashNavGraph.startDestination,
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
        ProfileNavGraph.startDestination,
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
