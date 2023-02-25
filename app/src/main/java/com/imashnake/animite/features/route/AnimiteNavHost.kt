package com.imashnake.animite.features.route

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.compose.NavHost
import com.github.uragiristereo.safer.compose.navigation.compose.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.imashnake.animite.features.home.Home
import com.imashnake.animite.features.media.MediaPage
import com.imashnake.animite.profile.ProfileScreen

@Composable
fun AnimiteNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AnimiteRoute.Home::class,
        modifier = modifier,
    ) {
        composable(
            route = AnimiteRoute.Home,
            disableDeserialization = true
        ) {
            Home(
                onNavigateToMediaPage = {
                    navController.navigate(it) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AnimiteRoute.MediaPage::class) {
            MediaPage()
        }

        composable(
            route = AnimiteRoute.Profile,
            disableDeserialization = true
        ) {
            ProfileScreen()
        }

        composable(
            route = AnimiteRoute.RSlash,
            disableDeserialization = true
        ) {
            ProfileScreen()
        }
    }
}
