package com.imashnake.animite.core.extensions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
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

/**
 *  A custom [Modifier] that draws a background behind the status bar area
 *  which gradually fades in as the user scrolls.
 */
@Composable
fun Modifier.statusBarScrollBackground(
    distanceUntilAnimated: Dp,
    scrollState: ScrollState,
    targetAlpha: Float = ContentAlpha.medium,
    targetColor: Color = MaterialTheme.colorScheme.background,
): Modifier {
    val distancePx = with(LocalDensity.current){
        distanceUntilAnimated.toPx()
    }
    val statusBarInsets = WindowInsets.statusBars
    return this.drawWithContent {
        drawContent()
        drawRect(
            color = targetColor.copy(
                alpha = targetAlpha * if (scrollState.value < distancePx) {
                    scrollState.value.toFloat() / distancePx
                } else 1f
            ),
            size = Size(
                width = size.width,
                height = statusBarInsets
                    .getTop(this)
                    .toFloat()
            )
        )
    }
}