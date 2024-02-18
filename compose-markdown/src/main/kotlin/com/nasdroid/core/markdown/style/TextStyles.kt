package com.nasdroid.core.markdown.style

import androidx.compose.ui.text.TextStyle

/**
 * Describes the base set of text styles that can be rendered in the Markdown document. For
 * additional formatting, see [TextStyleModifiers].
 *
 * @property textStyle The standard text style. This is used for paragraphs.
 * @property headline1 The largest possible headline style.
 * @property headline2 The second largest possible headline style.
 * @property headline3 The third largest possible headline style.
 * @property headline4 The third smallest possible headline style.
 * @property headline5 The second smallest possible headline style.
 * @property headline6 The smallest possible headline style.
 */
data class TextStyles(
    val textStyle: TextStyle,
    val headline1: TextStyle,
    val headline2: TextStyle,
    val headline3: TextStyle,
    val headline4: TextStyle,
    val headline5: TextStyle,
    val headline6: TextStyle,
)

/**
 * Provides functions to modify a [TextStyle], producing a style for a particular text format. It is
 * recommended to modify the provided TextStyle, which may already be stylized for other formatting.
 *
 * @property bold A function that produces a [TextStyle] for bold text.
 * @property italics A function that produces a [TextStyle] for italicized text.
 * @property strikethrough A function that produces a [TextStyle] for strikethrough text.
 * @property link A function that produces a [TextStyle] for clickable link text.
 * @property code A function that produces a [TextStyle] for inline code text.
 */
data class TextStyleModifiers(
    val bold: (TextStyle) -> TextStyle,
    val italics: (TextStyle) -> TextStyle,
    val strikethrough: (TextStyle) -> TextStyle,
    val link: (TextStyle) -> TextStyle,
    val code: (TextStyle) -> TextStyle,
)
