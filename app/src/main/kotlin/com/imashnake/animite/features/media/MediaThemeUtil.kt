package com.imashnake.animite.features.media

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme

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
) = color?.let {
    rememberDynamicColorScheme(
        seedColor = Color(it),
        isDark = isDark,
        isAmoled = false,
        style = PaletteStyle.Vibrant,
    )
} ?: fallbackColorScheme
