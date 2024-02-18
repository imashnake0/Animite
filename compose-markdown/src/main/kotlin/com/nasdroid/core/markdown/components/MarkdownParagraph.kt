package com.nasdroid.core.markdown.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.nasdroid.core.markdown.generator.MarkdownParagraph
import com.nasdroid.core.markdown.style.TextStyleModifiers
import com.nasdroid.core.markdown.style.TextUnitSize

/**
 * Displays a [MarkdownParagraph]. A paragraph is a group of "spans". Spans are stylized sections of
 * text, but can also include inline images and links.
 */
@Composable
fun MarkdownParagraph(
    paragraph: MarkdownParagraph,
    textStyle: TextStyle,
    textStyleModifiers: TextStyleModifiers,
    modifier: Modifier = Modifier,
) {
    val (annotatedString, inlineContent) = remember(paragraph) {
        paragraph.children.buildTextWithContent(textStyle, textStyleModifiers, TextUnitSize(100.sp, 100.sp))
    }
    BasicText(
        text = annotatedString,
        modifier = modifier,
        inlineContent = inlineContent
    )
}
