package com.imashnake.animite.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation3.runtime.NavKey

enum class NavigationBarPaths(
    val route: NavKey,
    @param:DrawableRes val icon: Int,
    @param:StringRes val iconDescription: Int,
) {
    Social(
        route = SocialRoute,
        icon = R.drawable.social,
        iconDescription = R.string.social,
    ),
    Anime(
        route = AnimeRoute,
        icon = R.drawable.anime,
        iconDescription = R.string.anime,
    ),
    Manga(
        route = MangaRoute,
        icon = R.drawable.manga,
        iconDescription = R.string.manga,
    ),
    Profile(
        route = ProfileRoute(),
        icon = R.drawable.profile,
        iconDescription = R.string.profile,
    ),
}
