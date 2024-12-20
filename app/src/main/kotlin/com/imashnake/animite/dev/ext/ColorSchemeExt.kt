package com.imashnake.animite.dev.ext

import androidx.compose.material3.ColorScheme

/**
 * @see [Scheme.pastelize].
 */
fun ColorScheme.pastelize(backgroundToPrimary: Float) = with(this) {
    copy(background = background.blendWith(primary, backgroundToPrimary))
}
