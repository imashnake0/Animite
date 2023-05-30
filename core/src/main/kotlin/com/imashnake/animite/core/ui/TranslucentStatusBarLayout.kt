package com.imashnake.animite.core.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun TranslucentStatusBarLayout(
    scrollState: ScrollState,
    distanceUntilAnimated: Dp,
    modifier: Modifier = Modifier,
    targetAlpha: Float = ContentAlpha.medium,
    targetColor: Color = MaterialTheme.colorScheme.scrim,
    content: @Composable () -> Unit,
) {
    // TODO: Can this be a modifier?
    val distanceUntilAnimatedPx = with(LocalDensity.current) { distanceUntilAnimated.toPx() }
    val statusBarInsets = WindowInsets.statusBars
    Box(
        Modifier.drawWithContent {
            drawContent()
            drawRect(
                color = targetColor.copy(
                    alpha = targetAlpha * if (scrollState.value < distanceUntilAnimatedPx) {
                        scrollState.value.toFloat() / distanceUntilAnimatedPx
                    } else 1f
                ),
                size = Size(
                    width = size.width,
                    height = statusBarInsets.getTop(this).toFloat()
                )
            )
        }.then(modifier)
    ) {
        content()
    }
}
