package com.imashnake.animite.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

// TODO: Replace with a NavDisplay Scene + TransitionSpec
public val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
    staticCompositionLocalOf {
        // no default, we need an SharedTransition to get the SharedTransitionScope
        throw IllegalStateException(
            "Unexpected access to SharedTransitionScope. You should only " +
                    "access LocalSharedTransitionScope inside a NavEntry passed " +
                    "to NavDisplay."
        )
    }