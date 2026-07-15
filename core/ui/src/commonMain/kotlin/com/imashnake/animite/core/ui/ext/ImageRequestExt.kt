package com.imashnake.animite.core.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.imashnake.animite.core.ui.Constants

/**
 * Crossfades images after the request succeeds.
 *
 * @param model is the argument of [ImageRequest.Builder.data].
 */
@Composable
fun crossfadeModel(model: String?) = ImageRequest
    .Builder(LocalContext.current)
    .data(model)
    .crossfade(Constants.CROSSFADE_DURATION)
    .build()
