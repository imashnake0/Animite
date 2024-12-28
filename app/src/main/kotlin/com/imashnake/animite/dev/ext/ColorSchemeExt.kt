package com.imashnake.animite.dev.ext

import androidx.compose.material3.ColorScheme

fun ColorScheme.pastelize(backgroundToPrimary: Float) = with(this) {
    copy(background = background.blendWith(primary, backgroundToPrimary))
}
