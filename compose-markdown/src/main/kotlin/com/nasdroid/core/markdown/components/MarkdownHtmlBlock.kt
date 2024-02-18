package com.nasdroid.core.markdown.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.nasdroid.core.markdown.generator.MarkdownHtmlBlock

/**
 * Displays a [MarkdownHtmlBlock]. An HTML block is a block of text that is formatted with HTML.
 * Unfortunately we don't (yet) render this HTML to anything useful, so we just display the raw
 * text.
 */
@Composable
fun MarkdownHtmlBlock(
    htmlBlock: MarkdownHtmlBlock,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    BasicText(
        text = htmlBlock.text,
        style = textStyle,
        modifier = modifier
    )
}
