package com.imashnake.animite.features.media

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.imashnake.animite.features.theme.toMaterialColorScheme
import contrast.Contrast
import hct.Hct
import scheme.SchemeNeutral

/**
 * Remembers a new Material3 [ColorScheme] based on the given color, or falls back to the default
 * color scheme.
 *
 * @param color The color to create and remember a color scheme for.
 * @param fallbackColorScheme The color scheme to fall back to if [color] is null.
 * @param isDark Whether the new color scheme is to be used in dark mode.
 */
@Composable
fun rememberColorSchemeFor(
    color: Int?,
    fallbackColorScheme: ColorScheme = MaterialTheme.colorScheme,
    isDark: Boolean = isSystemInDarkTheme()
): ColorScheme {
    return remember(color, isDark) {
        if (color != null) {
            SchemeNeutral(Hct.fromInt(color), isDark, Contrast.RATIO_MAX).toMaterialColorScheme()
        } else {
            fallbackColorScheme
        }
    }
}
