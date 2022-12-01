package com.imashnake.animite.core.extensions

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration

fun Modifier.given(
    boolean: Boolean, execute: Modifier.() -> Modifier
): Modifier = if (boolean) {
    then(execute(Modifier))
} else {
    this
}

fun Modifier.landscapeCutoutPadding() = composed {
    Modifier.given(
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    ) { displayCutoutPadding() }
}

fun Modifier.bannerParallax(scrollState: ScrollState) = graphicsLayer {
    translationY = 0.7f * scrollState.value
}
