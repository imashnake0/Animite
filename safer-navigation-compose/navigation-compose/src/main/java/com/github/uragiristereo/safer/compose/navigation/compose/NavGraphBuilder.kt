package com.github.uragiristereo.safer.compose.navigation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.Serializer
import com.github.uragiristereo.safer.compose.navigation.core.Util
import com.github.uragiristereo.safer.compose.navigation.core.route
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
inline fun <reified T : NavRoute> NavGraphBuilder.composable(
    route: T,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable NavBackStackEntry.(T) -> Unit,
) {
    val klass = route::class as KClass<T>

    Serializer.registerRoute(klass)

    composable(
        route = klass.route,
        arguments = listOf(Util.namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            val data = remember(entry) { Util.getDataOrNull(klass, entry) }

            content(entry, data ?: route)
        },
    )
}

@Suppress("UNUSED_PARAMETER", "UNCHECKED_CAST")
inline fun <reified T : NavRoute> NavGraphBuilder.composable(
    route: T,
    disableDeserialization: Boolean,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable NavBackStackEntry.() -> Unit,
) {
    val klass = route::class as KClass<T>

    Serializer.registerRoute(klass)

    composable(
        route = klass.route,
        arguments = listOf(Util.namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            content(entry)
        },
    )
}

inline fun <reified T : NavRoute> NavGraphBuilder.composable(
    route: KClass<T>,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable NavBackStackEntry.(T?) -> Unit,
) {
    Serializer.registerRoute(route)

    composable(
        route = route.route,
        arguments = listOf(Util.namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            val data = remember(entry) { Util.getDataOrNull(route, entry) }

            content(entry, data)
        },
    )
}

@Suppress("UNUSED_PARAMETER")
inline fun <reified T : NavRoute> NavGraphBuilder.composable(
    route: KClass<T>,
    disableDeserialization: Boolean,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable NavBackStackEntry.() -> Unit,
) {
    Serializer.registerRoute(route)

    composable(
        route = route.route,
        arguments = listOf(Util.namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            content(entry)
        },
    )
}

inline fun <reified A : NavRoute, reified B : NavRoute> NavGraphBuilder.navigation(
    startDestination: KClass<A>,
    route: KClass<B>,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline builder: NavGraphBuilder.() -> Unit,
) {
    Serializer.registerRoute(route)

    navigation(
        startDestination = startDestination.route,
        route = route.route,
        arguments = listOf(Util.namedNavArg),
        deepLinks = deepLinks,
        builder = builder,
    )
}
