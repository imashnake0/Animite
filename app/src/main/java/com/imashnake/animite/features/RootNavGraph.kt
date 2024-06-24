package com.imashnake.animite.features

//import com.imashnake.animite.features.destinations.HomeScreenDestination
//import com.imashnake.animite.features.destinations.MediaPageDestination
//import com.imashnake.animite.profile.ProfileNavGraph
//import com.imashnake.animite.rslash.RslashNavGraph
//import com.ramcosta.composedestinations.spec.DestinationSpec
//import com.ramcosta.composedestinations.spec.NavGraphSpec
//import com.ramcosta.composedestinations.spec.Route

// TODO: New way of setting up compose-destinations for multiple modules:
//  https://composedestinations.rafaelcosta.xyz/v2/multi-module-setup
//object RootNavGraph : NavGraphSpec {
//    override val route: String = "root"
//
//    // TODO Once Home is refactored to a module, turn off compose-destinations code gen for app module
//    override val destinationsByRoute: Map<String, DestinationSpec<*>> = mapOf(
//        HomeScreenDestination.route to HomeScreenDestination,
//        MediaPageDestination.route to MediaPageDestination
//    )
//
//    override val startRoute: Route = HomeScreenDestination
//
//    override val nestedNavGraphs: List<NavGraphSpec> = listOf(
//        ProfileNavGraph,
//        RslashNavGraph
//    )
//}
