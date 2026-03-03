package com.imashnake.animite.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Paddings(
    val ultraTiny: Dp,
    val tiny: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp
) {
    companion object {
        val ZERO = 0.dp
    }
}

val LocalPaddings = staticCompositionLocalOf<Paddings> { error("Paddings must be set!") }

@Composable
fun rememberDefaultPaddings(): Paddings {
    return Paddings(
        ultraTiny = 1.dp,
        tiny = 4.dp,
        small = 8.dp,
        medium = 16.dp,
        large = 24.dp
    )
}
