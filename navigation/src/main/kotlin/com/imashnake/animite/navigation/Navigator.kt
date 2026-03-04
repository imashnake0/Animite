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
        if (!allowMultiple && backStack.contains(navKey)) return

        if (NavigationBarPaths.entries.any { it.route == navKey }) {
            backStack.clear()
        }

        backStack.add(navKey)
    }

    fun popBack() {
        backStack.removeLastOrNull()
    }
}
