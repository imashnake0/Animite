package com.imashnake.animite.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.scopes.ActivityRetainedScoped

@ActivityRetainedScoped
class Navigator(
    startDestination: NavKey,
) {

    val backStack = NavBackStack(startDestination)

    fun navigate(navKey: NavKey, allowMultiple: Boolean = false) {
        when {
            navKey is AnimeRoute -> {
                if (backStack.lastOrNull() is AnimeRoute) return popBackTo(AnimeRoute)
            }
            !allowMultiple && backStack.contains(navKey) -> return
        }

        if (NavigationBarPaths.entries.any { it.route == navKey }) {
            backStack.removeAll { it != AnimeRoute }
        }

        backStack.add(navKey)
    }

    fun onBackPressed() {
        if (backStack.lastOrNull() is AnimeRoute) {
            backStack.clear()
        } else {
            backStack.removeLastOrNull()
        }
    }

    fun popBackTo(navKey: NavKey) {
        backStack.removeAll { it != navKey }
    }
}
