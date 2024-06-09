package com.imashnake.animite.core.ui.layouts

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.imashnake.animite.core.R

@Composable
@Suppress("LongParameterList")
fun TranslucentStatusBarLayout(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    distanceUntilAnimated: Dp = dimensionResource(R.dimen.banner_height),
    targetAlpha: Float = ContentAlpha.medium,
    targetColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable BoxScope.() -> Unit
) {
    // TODO: Can this be a modifier?
    val distanceUntilAnimatedPx = with(LocalDensity.current) { distanceUntilAnimated.toPx() }
    val statusBarInsets = WindowInsets.statusBars
    Box(
        Modifier
            .drawWithContent {
                drawContent()
                drawRect(
                    color = targetColor.copy(
                        alpha = targetAlpha * if (scrollState.value < distanceUntilAnimatedPx) {
                            scrollState.value.toFloat() / distanceUntilAnimatedPx
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
            .then(modifier)
    ) {
        content()
    }
}
