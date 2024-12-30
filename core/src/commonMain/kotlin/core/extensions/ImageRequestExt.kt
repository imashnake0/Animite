package core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import core.Constants

/**
 * Crossfades images after the request succeeds.
 *
 * @param model is the argument of [ImageRequest.Builder.data].
 */
@Composable
fun crossfadeModel(model: Any?) = ImageRequest
    .Builder(LocalContext.current)
    .data(model)
    .crossfade(Constants.CROSSFADE_DURATION)
    .build()
