package com.github.uragiristereo.safer.compose.navigation.core

import kotlin.reflect.KClass

interface NavRoute

inline val KClass<out NavRoute>.route: String
    get() = this.java.name
        .lowercase()
        .replace(oldChar = '.', newChar = '/')
        .replace(oldChar = '$', newChar = '/') + "?data={data}"

@get:JvmName("NavRouteGetRoute")
val NavRoute.route: String
    get() = this::class.route

@get:JvmName("NavRouteGetRouteNullable")
val NavRoute?.route: String?
    get() = this?.let { it::class.route }

internal fun NavRoute.parseData(): String {
    val withData = !Util.isClassAnObject(this::class)
    var route = this::class.route

    if (withData) {
        val encoded = Serializer.encode(value = this)
        route = route.replaceFirst(oldValue = "{data}", newValue = encoded)
    }

    return route
}
