package com.imashnake.animite.media.ext

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.materialkolor.ktx.blend

fun ColorScheme.modify(useDarkTheme: Boolean, isAmoled: Boolean): ColorScheme {
    when {
        useDarkTheme -> copy(surfaceContainerHighest = surfaceContainerHighest.blend(background, 0.5f))
        else -> copy(background = background.blend(primary, 0.05f))
    }
    if (isAmoled) {
        val amoledColor = if (useDarkTheme) Color.Black else Color.White
        val onAmoledColor = if (useDarkTheme) Color.White else Color.Black
        return copy(
            background = amoledColor,
            onBackground = onAmoledColor,
            surface = amoledColor,
            onSurface = onAmoledColor,
        )
    }
    return this
}
