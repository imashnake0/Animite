package com.imashnake.animite.features.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import scheme.DynamicScheme

fun DynamicScheme.toMaterialColorScheme(): ColorScheme {
    return if (!isDark) ColorScheme(
        primary = Color(primaryPalette.tone(40)),
        primaryContainer = Color(primaryPalette.tone(90)),
        secondary = Color(secondaryPalette.tone(40)),
        secondaryContainer = Color(secondaryPalette.tone(90)),
        tertiary = Color(tertiaryPalette.tone(40)),
        tertiaryContainer = Color(tertiaryPalette.tone(90)),
        surface = Color(neutralPalette.tone(99)),
        surfaceVariant = Color(neutralVariantPalette.tone(90)),
        background = Color(neutralPalette.tone(99)),
        error = Color(errorPalette.tone(40)),
        errorContainer = Color(errorPalette.tone(90)),
        onPrimary = Color(primaryPalette.tone(100)),
        onPrimaryContainer = Color(primaryPalette.tone(10)),
        onSecondary = Color(secondaryPalette.tone(100)),
        onSecondaryContainer = Color(secondaryPalette.tone(10)),
        onTertiary = Color(tertiaryPalette.tone(100)),
        onTertiaryContainer = Color(tertiaryPalette.tone(10)),
        onSurface = Color(neutralPalette.tone(10)),
        onSurfaceVariant = Color(neutralVariantPalette.tone(30)),
        onError = Color(errorPalette.tone(100)),
        onErrorContainer = Color(errorPalette.tone(10)),
        onBackground = Color(neutralPalette.tone(10)),
        outline = Color(neutralVariantPalette.tone(50)),
        outlineVariant = Color(neutralVariantPalette.tone(80)),
        scrim = Color(neutralPalette.tone(0)),
        surfaceTint = Color(primaryPalette.tone(40)),
        inverseSurface = Color(neutralPalette.tone(20)),
        inverseOnSurface = Color(neutralPalette.tone(95)),
        inversePrimary = Color(primaryPalette.tone(80))
    ) else ColorScheme(
        primary = Color(primaryPalette.tone(80)),
        primaryContainer = Color(primaryPalette.tone(30)),
        secondary = Color(secondaryPalette.tone(80)),
        secondaryContainer = Color(secondaryPalette.tone(30)),
        tertiary = Color(tertiaryPalette.tone(80)),
        tertiaryContainer = Color(tertiaryPalette.tone(30)),
        surface = Color(neutralPalette.tone(10)),
        surfaceVariant = Color(neutralVariantPalette.tone(30)),
        background = Color(neutralPalette.tone(10)),
        error = Color(errorPalette.tone(80)),
        errorContainer = Color(errorPalette.tone(30)),
        onPrimary = Color(primaryPalette.tone(20)),
        onPrimaryContainer = Color(primaryPalette.tone(90)),
        onSecondary = Color(secondaryPalette.tone(20)),
        onSecondaryContainer = Color(secondaryPalette.tone(90)),
        onTertiary = Color(tertiaryPalette.tone(20)),
        onTertiaryContainer = Color(tertiaryPalette.tone(90)),
        onSurface = Color(neutralPalette.tone(90)),
        onSurfaceVariant = Color(neutralVariantPalette.tone(80)),
        onError = Color(errorPalette.tone(20)),
        onErrorContainer = Color(errorPalette.tone(90)),
        onBackground = Color(neutralPalette.tone(90)),
        outline = Color(neutralVariantPalette.tone(60)),
        outlineVariant = Color(neutralVariantPalette.tone(30)),
        scrim = Color(neutralPalette.tone(0)),
        surfaceTint = Color(primaryPalette.tone(80)),
        inverseSurface = Color(neutralPalette.tone(90)),
        inverseOnSurface = Color(neutralPalette.tone(20)),
        inversePrimary = Color(primaryPalette.tone(40))
    )
}