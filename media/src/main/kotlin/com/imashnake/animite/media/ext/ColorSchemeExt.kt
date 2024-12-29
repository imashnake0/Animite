package com.imashnake.animite.media.ext

import androidx.compose.material3.ColorScheme
import com.materialkolor.ktx.blend

fun ColorScheme.modify(useDarkTheme: Boolean) = when {
    useDarkTheme -> copy(surfaceContainerHighest = surfaceContainerHighest.blend(background, 0.5f))
    else -> copy(background = background.blend(primary, 0.05f))
}
