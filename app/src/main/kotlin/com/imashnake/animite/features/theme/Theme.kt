package com.imashnake.animite.features.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.imashnake.animite.core.Constants.PASTELIZE_RATIO
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.Paddings
import com.imashnake.animite.core.ui.rememberDefaultPaddings
import com.imashnake.animite.dev.ext.pastelize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimiteTheme(
    paddings: Paddings = rememberDefaultPaddings(),
    content: @Composable () -> Unit
) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val darkTheme = isSystemInDarkTheme()
    val animiteColorScheme = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
            .pastelize(PASTELIZE_RATIO)
        darkTheme -> KimiNoDarkColorScheme
        else -> KimiNoLightColorScheme
            .pastelize(PASTELIZE_RATIO)
    }
    val animiteRippleTheme = RippleConfiguration(
        color = MaterialTheme.colorScheme.primary,
        rippleAlpha = RippleAlpha(
            draggedAlpha = 0.16f,
            focusedAlpha = 0.12f,
            hoveredAlpha = 0.08f,
            pressedAlpha = 0.12f
        )
    )

    MaterialTheme(
        colorScheme = animiteColorScheme,
        typography = AnimiteTypography
    ) {
        CompositionLocalProvider(
            LocalRippleConfiguration provides animiteRippleTheme,
            LocalPaddings provides paddings,
            content = content
        )
    }
}
