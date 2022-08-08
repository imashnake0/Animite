package com.imashnake.animite.dev.internal

import androidx.annotation.StringRes
import com.imashnake.animite.R as Rpath

/**
 * There are three [Path]s that the user can take in the app: [Home], [Profile], and [RSlash].
 *
 * @param route
 * Used by [NavHost][androidx.navigation.compose.NavHost]; see
 * [Navigating with Compose](https://developer.android.com/jetpack/compose/navigation):
 *
 * *Route is a String that defines the path to your composable. You can think of it as an implicit
 * deep link that leads to a specific destination. Each destination should have a unique route.*
 *
 * @param stringRes
 * This is what the path will be *called*. If the names of the paths are ever needed,
 * simply import this.
 */
sealed class Path(val route: String, @StringRes val stringRes: Int) {
    object Home: Path("home", Rpath.string.home)
    object Profile: Path("profile", Rpath.string.profile)
    object RSlash: Path("rslash", Rpath.string.rslash)
}
