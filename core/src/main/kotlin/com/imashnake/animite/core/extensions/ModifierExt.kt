package com.imashnake.animite.core.extensions

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration

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
