package com.github.uragiristereo.safer.compose.navigation.core

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder
import kotlin.reflect.KClass

fun NavOptionsBuilder.popUpTo(
    route: KClass<NavRoute>,
    popUpToBuilder: PopUpToBuilder.() -> Unit,
) {
    popUpTo(
        route = route.route,
        popUpToBuilder = popUpToBuilder,
    )
}
