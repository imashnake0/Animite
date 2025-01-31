package com.imashnake.animite.components.extensions

import androidx.compose.runtime.Composable
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

const val CROSSFADE_DURATION = 500

/**
 * Crossfades images after the request succeeds.
 *
 * @param model is the argument of [ImageRequest.Builder.data].
 */
@Composable
fun crossfadeModel(model: Any?) = ImageRequest
    .Builder(LocalPlatformContext.current)
    .data(model)
    .crossfade(CROSSFADE_DURATION)
    .build()
