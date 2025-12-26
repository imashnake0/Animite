package com.imashnake.animite.features.theme

import android.os.Build
import androidx.compose.animation.core.tween
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.Paddings
import com.imashnake.animite.core.ui.rememberDefaultPaddings
import com.imashnake.animite.media.ext.modify
import com.materialkolor.PaletteStyle
import com.materialkolor.ktx.animateColorScheme
import com.materialkolor.rememberDynamicColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimiteTheme(
    paddings: Paddings = rememberDefaultPaddings(),
    useDarkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val context = LocalContext.current
    val staticColorScheme = rememberDynamicColorScheme(
        seedColor = Color(0xFF2D7AB0),
        secondary = Color(0xFF687797),
        isDark = useDarkTheme,
        isAmoled = false,
        style = PaletteStyle.Vibrant,
    )
    val animiteColorScheme = remember(useDarkTheme) {
        when {
            dynamicColor && useDarkTheme -> dynamicDarkColorScheme(context)
            dynamicColor && !useDarkTheme -> dynamicLightColorScheme(context)
            else -> staticColorScheme
        }.modify(useDarkTheme)
    }

    val animiteRippleTheme = RippleConfiguration(
        color = MaterialTheme.colorScheme.primary,
        rippleAlpha = RippleAlpha(
            draggedAlpha = 0.16f,
            focusedAlpha = 0.12f,
            hoveredAlpha = 0.08f,
            pressedAlpha = 0.12f,
        )
    )

    MaterialTheme(
        colorScheme = animateColorScheme(animiteColorScheme, animationSpec = { tween(500) }),
        typography = AnimiteTypography,
    ) {
        CompositionLocalProvider(
            LocalRippleConfiguration provides animiteRippleTheme,
            LocalPaddings provides paddings,
            content = content,
        )
    }
}
