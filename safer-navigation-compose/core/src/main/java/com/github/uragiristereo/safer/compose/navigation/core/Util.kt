package com.github.uragiristereo.safer.compose.navigation.core

import android.util.Log
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlin.reflect.KClass

object Util {
    inline fun <reified T : NavRoute> getDataOrNull(
        route: KClass<T>,
        entry: NavBackStackEntry,
    ): T? {
        val data = entry.arguments?.getString("data")

        return when {
            isClassAnObject(route) -> getObjectInstance(route)

            data == null -> {
                val e = IllegalArgumentException("Expecting navigation route data for \"${route.route}\" but got null!")

                Log.d("SaferNavigationCompose", "${e.message}")

                null
            }

            else -> Serializer.decode(data)
        }
    }

    fun isClassAnObject(klass: KClass<out NavRoute>): Boolean {
        return klass.java.declaredFields.any {
            it.type == klass.java && it.name == "INSTANCE"
        }
    }

    inline fun <reified T : Any> getObjectInstance(klass: KClass<T>): T {
        return klass.java.getDeclaredField("INSTANCE").get(null) as T
    }

    val namedNavArg = navArgument(name = "data") {
        type = NavType.StringType
        nullable = true
        defaultValue = null
    }
}
