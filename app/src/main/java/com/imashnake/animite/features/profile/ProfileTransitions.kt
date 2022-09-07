package com.imashnake.animite.features.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavBackStackEntry
import com.imashnake.animite.features.appDestination
import com.imashnake.animite.features.destinations.HomeDestination
import com.ramcosta.composedestinations.spec.DestinationStyle

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
object ProfileTransitions : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (initialState.appDestination()) {
            HomeDestination ->
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500)
                )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            HomeDestination ->
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500)
                )
            else -> null
        }
    }
}
