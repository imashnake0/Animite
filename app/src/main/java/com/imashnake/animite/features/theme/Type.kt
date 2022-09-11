package com.imashnake.animite.features.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.imashnake.animite.R

/**
 * [Manrope](https://manropefont.com/).
 */
val manropeFamily = FontFamily(
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_light, FontWeight.Light),
    Font(R.font.manrope_extralight, FontWeight.ExtraLight)
)

val AnimiteTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = (0.7).sp
    ),
    bodyMedium = TextStyle(
        fontSize = 10.sp,
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Medium,
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        letterSpacing = (1.5).sp,
    )
)
