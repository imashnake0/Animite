package com.imashnake.animite.core.extensions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.bannerParallax(scrollState: ScrollState) = graphicsLayer {
    translationY = 0.7f * scrollState.value
}

fun Modifier.maxHeight(max: Dp) = heightIn(0.dp, max)

/**
 * [Adding modifiers conditionally in Jetpack Compose](https://patrickmichalik.com/blog/adding-modifiers-conditionally-in-jetpack-compose).
 */
fun Modifier.thenIf(
    condition: Boolean,
    other: Modifier.() -> Modifier,
) = if (condition) other() else this
