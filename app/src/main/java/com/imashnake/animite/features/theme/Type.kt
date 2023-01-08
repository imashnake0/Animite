package com.imashnake.animite.features.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.sp
import com.imashnake.animite.R

/**
 * [Manrope](https://manropefont.com/).
 *
 * TODO:
 *  Use [Downloadable fonts](https://developer.android.com/jetpack/compose/text#downloadable-fonts)?
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

const val baselineShift = 0.2f
val AnimiteTypography = Typography(
    // - [Home]: Media list headings.
    // - [MediaPage]: Media section headings.
    titleMedium = TextStyle(
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        letterSpacing = 0.2.sp,
        baselineShift = BaselineShift(baselineShift)
    ),
    // - [Home]: Media list item labels.
    // - [MediaPage]: Character names.
    labelLarge = TextStyle(
        fontSize = 12.sp,
        fontFamily = manropeFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 18.sp,
        baselineShift = BaselineShift(baselineShift)
    ),
    // - [MediaPage]: Media title.
    titleLarge = TextStyle(
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.2.sp,
        baselineShift = BaselineShift(baselineShift)
    ),
    // - [MediaPage]: Media description.
    // - [SearchItem]: Search list item titles.
    // - [SearchItem]: Search list item studios.
    // - [SearchItem]: Search list item footer.
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 22.sp,
        baselineShift = BaselineShift(baselineShift)
    ),
    // - [MediaPage]: Stat label.
    // - [SearchItem]: Search list item season and year.
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Medium,
        baselineShift = BaselineShift(baselineShift)
    ),
    // - [MediaPage]: Stat score.
    displaySmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Bold,
        baselineShift = BaselineShift(baselineShift)
    ),
    // - [MediaPage]: Genre.
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        letterSpacing = (1.3).sp,
        baselineShift = BaselineShift(baselineShift)
    )
)
