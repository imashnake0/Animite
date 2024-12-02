package com.imashnake.media

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import extensions.from
import extensions.pastelize
import utils.ThemeUtils

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
            fallbackColorScheme.from(
                if (isDark)
                    ThemeUtils.themeFromSourceColor(color).schemes.dark
                else
                    ThemeUtils.themeFromSourceColor(color).schemes.light.pastelize(
                        backgroundToPrimary = 0.09f
                    )
            )
        } else {
            fallbackColorScheme
        }
    }
}
