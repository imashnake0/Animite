package com.imashnake.animite.features.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalContext
import com.imashnake.animite.dev.ext.pastelize

@Composable
fun AnimiteTheme(content: @Composable () -> Unit) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val darkTheme = isSystemInDarkTheme()
    val animiteColorScheme = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current).pastelize(
            backgroundToPrimary = 0.09f
        )
        darkTheme -> KimiNoDarkColorScheme
        else -> KimiNoLightColorScheme.pastelize(backgroundToPrimary = 0.09f)
    }

    MaterialTheme(
        colorScheme = animiteColorScheme,
        typography = AnimiteTypography
    ) {
        CompositionLocalProvider(
            LocalRippleTheme provides AnimiteRippleTheme,
            content = content
        )
    }
}

@Immutable
private object AnimiteRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = MaterialTheme.colorScheme.primary

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = 0.16f,
        focusedAlpha = 0.12f,
        hoveredAlpha = 0.08f,
        pressedAlpha = 0.12f
    )
}
