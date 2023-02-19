package com.imashnake.animite.features

import com.imashnake.animite.features.destinations.HomeDestination
import com.imashnake.animite.features.destinations.MediaScreenDestination
import com.imashnake.animite.profile.ProfileNavGraph
import com.imashnake.animite.rslash.RslashNavGraph
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

object RootNavGraph : NavGraphSpec {
    override val route: String = "root"

    // TODO Once Home is refactored to a module, turn off compose-destinations code gen for app module
    override val destinationsByRoute: Map<String, DestinationSpec<*>> = mapOf(
        HomeDestination.route to HomeDestination,
        MediaScreenDestination.route to MediaScreenDestination
    )

    override val startRoute: Route = HomeDestination

    override val nestedNavGraphs: List<NavGraphSpec> = listOf(
        ProfileNavGraph,
        RslashNavGraph
    )
}
