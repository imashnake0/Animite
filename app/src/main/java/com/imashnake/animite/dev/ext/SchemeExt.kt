package com.imashnake.animite.dev.ext

import androidx.core.graphics.ColorUtils
import scheme.Scheme

fun Scheme.pastelize(background: Float) =
    Scheme(
        primary = this.primary,
        onPrimary = this.onPrimary,
        primaryContainer = this.primaryContainer,
        onPrimaryContainer = this.onPrimaryContainer,
        secondary = this.secondary,
        onSecondary = this.onSecondary,
        secondaryContainer = this.secondaryContainer,
        onSecondaryContainer = this.onSecondaryContainer,
        tertiary = this.tertiary,
        onTertiary = this.onTertiary,
        tertiaryContainer = this.tertiaryContainer,
        onTertiaryContainer = this.onTertiaryContainer,
        error = this.error,
        onError = this.onError,
        errorContainer = this.errorContainer,
        onErrorContainer = this.onErrorContainer,
        background = ColorUtils.blendARGB(this.background, this.primaryContainer, background),
        onBackground = this.onBackground,
        surface = this.surface,
        onSurface = this.onSurface,
        surfaceVariant = this.surfaceVariant,
        onSurfaceVariant = this.onSurfaceVariant,
        outline = this.outline,
        outlineVariant = this.outlineVariant,
        shadow = this.shadow,
        scrim = this.scrim,
        inverseSurface = this.inverseSurface,
        inverseOnSurface = this.inverseOnSurface,
        inversePrimary = this.inversePrimary,
    )
