package com.imashnake.animite.features.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun AnimiteTheme(content: @Composable () -> Unit) {
    // TODO: Create a colorScheme for < API 31:
    //  https://developer.android.com/jetpack/compose/themes/material#m3-dynamic-color.
    val colorScheme = dynamicDarkColorScheme(LocalContext.current)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AnimiteTypography,
        content = content
    )
}
