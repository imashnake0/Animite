package com.imashnake.animite.features.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.sp
import com.imashnake.animite.R

internal val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

internal val manropeFont = GoogleFont("Manrope")

/**
 * [Manrope](https://manropefont.com/).
 */
val manropeFontFamily = FontFamily(
    Font(manropeFont, fontProvider, FontWeight.ExtraBold),
    Font(manropeFont, fontProvider, FontWeight.Bold),
    Font(manropeFont, fontProvider, FontWeight.SemiBold),
    Font(manropeFont, fontProvider, FontWeight.Medium),
    Font(manropeFont, fontProvider, FontWeight.Normal),
    Font(manropeFont, fontProvider, FontWeight.Light),
    Font(manropeFont, fontProvider, FontWeight.ExtraLight),
    Font(R.font.manrope_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.manrope_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.manrope_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.manrope_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.manrope_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.manrope_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.manrope_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
)

const val BASELINE_SHIFT = 0.2f
val AnimiteTypography = Typography(
    // - [AnimeScreen, MangaScreen]: Media list headings.
    // - [MediaPage]: Media section headings.
    titleMedium = TextStyle(
        fontFamily = manropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        letterSpacing = 0.2.sp,
        baselineShift = BaselineShift(BASELINE_SHIFT)
    ),
    // - [AnimeScreen, MangaScreen]: Media list item labels.
    // - [MediaPage]: Character names.
    labelLarge = TextStyle(
        fontSize = 12.sp,
        fontFamily = manropeFontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 18.sp,
        baselineShift = BaselineShift(BASELINE_SHIFT)
    ),
    // - [MediaPage]: Media title.
    titleLarge = TextStyle(
        fontFamily = manropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.2.sp,
        baselineShift = BaselineShift(BASELINE_SHIFT)
    ),
    // - [MediaPage]: Media description.
    // - [SearchItem]: Search list item titles.
    // - [SearchItem]: Search list item studios.
    // - [SearchItem]: Search list item footer.
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = manropeFontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 22.sp,
        baselineShift = BaselineShift(BASELINE_SHIFT)
    ),
    // - [MediaPage]: Stat label.
    // - [SearchItem]: Search list item season and year.
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = manropeFontFamily,
        fontWeight = FontWeight.Medium,
        baselineShift = BaselineShift(BASELINE_SHIFT)
    ),
    // - [MediaPage]: Stat score.
    displaySmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = manropeFontFamily,
        fontWeight = FontWeight.Bold,
        baselineShift = BaselineShift(BASELINE_SHIFT)
    ),
    // - [MediaPage]: Genre.
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.3.sp,
        baselineShift = BaselineShift(BASELINE_SHIFT)
    )
)
