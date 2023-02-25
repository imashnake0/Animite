package com.github.uragiristereo.safer.compose.navigation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
inline fun <reified T : NavRoute> NavGraphBuilder.dialog(
    route: T,
    deepLinks: List<NavDeepLink> = listOf(),
    dialogProperties: DialogProperties = DialogProperties(),
    noinline content: @Composable NavBackStackEntry.(T) -> Unit,
) {
    val klass = route::class as KClass<T>

    Serializer.registerRoute(klass)

    dialog(
        route = klass.route,
        arguments = listOf(Util.namedNavArg),
        deepLinks = deepLinks,
        dialogProperties = dialogProperties,
        content = { entry ->
            val data = remember(entry) { Util.getDataOrNull(klass, entry) }

            content(entry, data ?: route)
        },
    )
}

@Suppress("UNUSED_PARAMETER", "UNCHECKED_CAST")
inline fun <reified T : NavRoute> NavGraphBuilder.dialog(
    route: T,
    disableDeserialization: Boolean,
    deepLinks: List<NavDeepLink> = listOf(),
    dialogProperties: DialogProperties = DialogProperties(),
    noinline content: @Composable NavBackStackEntry.() -> Unit,
) {
    val klass = route::class as KClass<T>

    Serializer.registerRoute(klass)

    dialog(
        route = klass.route,
        arguments = listOf(Util.namedNavArg),
        deepLinks = deepLinks,
        dialogProperties = dialogProperties,
        content = { entry ->
            content(entry)
        },
    )
}

inline fun <reified T : NavRoute> NavGraphBuilder.dialog(
    route: KClass<T>,
    deepLinks: List<NavDeepLink> = listOf(),
    dialogProperties: DialogProperties = DialogProperties(),
    noinline content: @Composable NavBackStackEntry.(T?) -> Unit,
) {
    Serializer.registerRoute(route)

    dialog(
        route = route.route,
        arguments = listOf(Util.namedNavArg),
        deepLinks = deepLinks,
        dialogProperties = dialogProperties,
        content = { entry ->
            val data = remember(entry) { Util.getDataOrNull(route, entry) }

            content(entry, data)
        },
    )
}

@Suppress("UNUSED_PARAMETER")
inline fun <reified T : NavRoute> NavGraphBuilder.dialog(
    route: KClass<T>,
    disableDeserialization: Boolean,
    deepLinks: List<NavDeepLink> = listOf(),
    dialogProperties: DialogProperties = DialogProperties(),
    noinline content: @Composable NavBackStackEntry.() -> Unit,
) {
    Serializer.registerRoute(route)

    dialog(
        route = route.route,
        arguments = listOf(Util.namedNavArg),
        deepLinks = deepLinks,
        dialogProperties = dialogProperties,
        content = { entry ->
            content(entry)
        },
    )
}
