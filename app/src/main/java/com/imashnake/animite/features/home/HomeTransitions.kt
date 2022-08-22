package com.imashnake.animite.features.home

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavBackStackEntry
import com.imashnake.animite.features.appDestination
import com.imashnake.animite.features.destinations.ProfileDestination
import com.ramcosta.composedestinations.spec.DestinationStyle

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
object HomeTransitions : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return when (initialState.appDestination()) {
            ProfileDestination ->
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                )
            else -> null
        }
    }

    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            ProfileDestination ->
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(500)
                )
            else -> null
        }
    }
}
