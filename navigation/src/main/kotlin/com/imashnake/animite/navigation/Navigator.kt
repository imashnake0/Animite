package com.imashnake.animite.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.scopes.ActivityRetainedScoped

// TODO: do we want this, or a `rememberNavBackStack(startDestination)`?
@ActivityRetainedScoped
class Navigator(startDestination: NavKey) {

    val backStack = mutableStateListOf(startDestination)

    fun navigateTo(destination: NavKey) {
        if (backStack.lastOrNull() == destination) return

        if (destination is Root) {
            popBackToRoot()
        }
        backStack.add(destination)
    }

    fun popBack() = backStack.removeLastOrNull()

    fun popBackToRoot() = backStack.removeIf {
        it !is Root.SocialRoute &&
                it !is Root.MangaRoute &&
                it !is Root.AnimeRoute &&
                it !is Root.ProfileRoute
    }

    fun isCurrentScreenRoot(): Boolean {
        val current = backStack.lastOrNull()
        return current is Root.SocialRoute
                || current is Root.MangaRoute
                || current is Root.AnimeRoute
                || current is Root.ProfileRoute
    }
}