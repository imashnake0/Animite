package com.imashnake.animite.features

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.NavHost
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.imashnake.animite.data.AnimiteRoute
import com.imashnake.animite.features.home.Home
import com.imashnake.animite.features.media.MediaPage
import com.imashnake.animite.profile.ProfileScreen
import com.imashnake.animite.rslash.RSlashScreen

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
        composable<AnimiteRoute.Home> {
            Home(
                onNavigateToMediaPage = { id, mediaType ->
                    navController.navigate(
                        route = AnimiteRoute.MediaPage(id, mediaType),
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<AnimiteRoute.MediaPage> {
            MediaPage()
        }

        composable<AnimiteRoute.Profile> {
            ProfileScreen()
        }

        composable<AnimiteRoute.RSlash> {
            RSlashScreen()
        }
    }
}
