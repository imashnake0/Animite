package com.imashnake.animite.features.theme

import android.os.Build
import androidx.compose.animation.core.tween
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.imashnake.animite.core.extensions.DayPart.AFTERNOON
import com.imashnake.animite.core.extensions.DayPart.EVENING
import com.imashnake.animite.core.extensions.DayPart.MORNING
import com.imashnake.animite.core.extensions.DayPart.NIGHT
import com.imashnake.animite.core.extensions.toDayPart
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.Paddings
import com.imashnake.animite.core.ui.rememberDefaultPaddings
import com.imashnake.animite.media.ext.modify
import com.materialkolor.PaletteStyle
import com.materialkolor.ktx.animateColorScheme
import com.materialkolor.rememberDynamicColorScheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class, ExperimentalTime::class,
)
@Composable
fun AnimiteTheme(
    paddings: Paddings = rememberDefaultPaddings(),
    useDarkTheme: Boolean,
    useSystemColorScheme: Boolean,
    dayHour: Float?,
    content: @Composable () -> Unit,
) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && useSystemColorScheme
    val context = LocalContext.current

    // TODO: Make a top level function.
    val currentDayHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour.toFloat()

    val stops = when ((dayHour ?: currentDayHour).toDayPart()) {
        MORNING -> Triple(0xFF007695, 0xFFC8C4A3, 0xFFFFE8A5)
        AFTERNOON -> Triple(0xFF7AAEDD, 0xFFA1C8F5, 0xFFD1E4F6)
        EVENING -> Triple(0xFF5F81E2, 0xFFBF98B7, 0xFFCDACC2)
        NIGHT -> Triple(0xFF001020, 0xFF112B3A, 0xFF112B3A)
    }

    val staticColorScheme = rememberDynamicColorScheme(
        seedColor = Color(stops.first),
        primary = Color(stops.second),
        secondary = Color(stops.third),
        isDark = useDarkTheme,
        isAmoled = false,
        style = PaletteStyle.Vibrant,
    )
    val animiteColorScheme = remember(useDarkTheme, dynamicColor, stops) {
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

    MaterialExpressiveTheme(
        colorScheme = animateColorScheme(animiteColorScheme, animationSpec = { tween(500) }),
        typography = AnimiteTypography,
        motionScheme = MotionScheme.expressive()
    ) {
        CompositionLocalProvider(
            LocalRippleConfiguration provides animiteRippleTheme,
            LocalPaddings provides paddings,
            content = content,
        )
    }
}
