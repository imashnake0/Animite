package com.github.uragiristereo.safer.compose.navigation.core

import android.util.Log
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.navOptions

fun NavHostController.navigate(
    route: NavRoute,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    val data = route.parseData()
    val uri = NavDestination.createRoute(data).toUri()

    try {
        navigate(
            request = NavDeepLinkRequest.Builder
                .fromUri(uri)
                .build(),
            navOptions = navOptions,
            navigatorExtras = navigatorExtras,
        )
    } catch (e: IllegalArgumentException) {
        // When the data is too large it usually throws IllegalArgumentException "Navigation destination that matches request cannot be found"
        // So we're printing the error instead

        Log.e("SaferComposeNavigation", e.message.toString())
    }
}

fun NavHostController.navigate(
    route: NavRoute,
    builder: NavOptionsBuilder.() -> Unit,
) {
    navigate(
        route = route,
        navOptions = navOptions(builder),
    )
}
