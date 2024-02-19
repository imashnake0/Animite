package com.nasdroid.core.markdown.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nasdroid.core.markdown.MarkdownNode
import com.nasdroid.core.markdown.generator.MarkdownBlockQuote
import com.nasdroid.core.markdown.style.BlockQuoteStyle
import com.nasdroid.core.markdown.style.CodeBlockStyle
import com.nasdroid.core.markdown.style.TextStyleModifiers
import com.nasdroid.core.markdown.style.TextStyles
import com.nasdroid.core.markdown.toDp

/**
 * Displays a [MarkdownBlockQuote]. A block quote is a visually distinct section in a document,
 * usually used to reference external sources.
 */
@Composable
fun MarkdownBlockQuote(
    blockQuote: MarkdownBlockQuote,
    style: BlockQuoteStyle,
    textStyles: TextStyles,
    textStyleModifiers: TextStyleModifiers,
    codeBlockStyle: CodeBlockStyle,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .background(style.background, style.shape)
            .then(modifier)
    ) {
        Column(
            modifier = Modifier.padding(style.innerPadding),
            verticalArrangement = Arrangement.spacedBy(textStyles.textStyle.fontSize.toDp())
        ) {
            blockQuote.children.forEach {
                Row(Modifier.height(IntrinsicSize.Min)) {
                    Surface(
                        shape = CircleShape,
                        color = style.barColor,
                        modifier = Modifier
                            .padding(end = style.paddingAfterBar)
                            .width(style.barWidth)
                            .fillMaxHeight()
                    ) {}

                    MarkdownNode(
                        node = it,
                        textStyles = textStyles,
                        textStyleModifiers = textStyleModifiers,
                        blockQuoteStyle = style,
                        codeBlockStyle = codeBlockStyle
                    )
                }
            }
        }
    }
}
