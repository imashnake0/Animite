package com.imashnake.animite.core.extensions

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.landscapeCutoutPadding() = composed {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        displayCutoutPadding()
    } else {
        this
    }
}

fun Modifier.bannerParallax(scrollState: ScrollState) = graphicsLayer {
    translationY = 0.7f * scrollState.value
}

fun Modifier.maxHeight(max: Dp) = heightIn(0.dp, max)

/**
 * [Adding modifiers conditionally in Jetpack Compose](https://patrickmichalik.com/blog/adding-modifiers-conditionally-in-jetpack-compose).
 */
@Composable
fun Modifier.thenIf(
    condition: Boolean,
    elseOther: @Composable Modifier.() -> Modifier = { this },
    other: @Composable Modifier.() -> Modifier,
) = if (condition) other() else elseOther()
