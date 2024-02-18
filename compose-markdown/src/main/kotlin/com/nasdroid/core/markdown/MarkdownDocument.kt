package com.nasdroid.core.markdown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.nasdroid.core.markdown.components.MarkdownBlockQuote
import com.nasdroid.core.markdown.components.MarkdownCodeBlock
import com.nasdroid.core.markdown.components.MarkdownHeading
import com.nasdroid.core.markdown.components.MarkdownHtmlBlock
import com.nasdroid.core.markdown.components.MarkdownOrderedList
import com.nasdroid.core.markdown.components.MarkdownParagraph
import com.nasdroid.core.markdown.components.MarkdownRule
import com.nasdroid.core.markdown.components.MarkdownTable
import com.nasdroid.core.markdown.components.MarkdownUnorderedList
import com.nasdroid.core.markdown.generator.MarkdownBlockQuote
import com.nasdroid.core.markdown.generator.MarkdownCodeBlock
import com.nasdroid.core.markdown.generator.MarkdownHeading
import com.nasdroid.core.markdown.generator.MarkdownHtmlBlock
import com.nasdroid.core.markdown.generator.MarkdownNode
import com.nasdroid.core.markdown.generator.MarkdownNodeGenerator
import com.nasdroid.core.markdown.generator.MarkdownOrderedList
import com.nasdroid.core.markdown.generator.MarkdownParagraph
import com.nasdroid.core.markdown.generator.MarkdownRule
import com.nasdroid.core.markdown.generator.MarkdownTable
import com.nasdroid.core.markdown.generator.MarkdownUnorderedList
import com.nasdroid.core.markdown.style.BlockQuoteStyle
import com.nasdroid.core.markdown.style.CodeBlockStyle
import com.nasdroid.core.markdown.style.TextStyleModifiers
import com.nasdroid.core.markdown.style.TextStyles
import com.nasdroid.core.markdown.style.m3BlockQuoteStyle
import com.nasdroid.core.markdown.style.m3CodeBlockStyle
import com.nasdroid.core.markdown.style.m3TextStyleModifiers
import com.nasdroid.core.markdown.style.m3TextStyles
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

/**
 * Displays a Markdown document.
 */
@Composable
fun MarkdownDocument(
    markdown: String,
    textStyles: TextStyles,
    textStyleModifiers: TextStyleModifiers,
    blockQuoteStyle: BlockQuoteStyle,
    codeBlockStyle: CodeBlockStyle,
    modifier: Modifier = Modifier,
    sectionSpacing: Dp = textStyles.textStyle.fontSize.toDp()
) {
    val parsedMarkdownNodes = remember(markdown) {
        val flavor = GFMFlavourDescriptor()
        val tree = MarkdownParser(flavor).buildMarkdownTreeFromString(markdown)
        MarkdownNodeGenerator(markdown, tree).generateNodes()
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(sectionSpacing)
    ) {
        parsedMarkdownNodes.forEach {
            MarkdownNode(
                node = it,
                textStyles = textStyles,
                textStyleModifiers = textStyleModifiers,
                blockQuoteStyle = blockQuoteStyle,
                codeBlockStyle = codeBlockStyle
            )
        }
    }
}

@Composable
internal fun TextUnit.toDp(): Dp {
    return with(LocalDensity.current) {
        this@toDp.toPx().toDp()
    }
}

@Composable
internal fun MarkdownNode(
    node: MarkdownNode,
    textStyles: TextStyles,
    textStyleModifiers: TextStyleModifiers,
    blockQuoteStyle: BlockQuoteStyle,
    codeBlockStyle: CodeBlockStyle,
    modifier: Modifier = Modifier
) {
    when (node) {
        is MarkdownBlockQuote -> MarkdownBlockQuote(
            blockQuote = node,
            style = blockQuoteStyle,
            textStyles = textStyles,
            textStyleModifiers = textStyleModifiers,
            codeBlockStyle = codeBlockStyle,
            modifier = modifier,
        )
        is MarkdownCodeBlock -> MarkdownCodeBlock(
            codeBlock = node,
            style = codeBlockStyle,
            textStyle = textStyles.textStyle.copy(fontFamily = FontFamily.Monospace),
            modifier = modifier,
        )
        is MarkdownHeading -> MarkdownHeading(
            heading = node,
            modifier = modifier,
            textStyles = textStyles,
            textStyleModifiers = textStyleModifiers,
        )
        is MarkdownOrderedList -> MarkdownOrderedList(
            list = node,
            textStyles = textStyles,
            textStyleModifiers = textStyleModifiers,
            blockQuoteStyle = blockQuoteStyle,
            codeBlockStyle = codeBlockStyle,
            modifier = modifier
        )
        is MarkdownParagraph -> MarkdownParagraph(
            paragraph = node,
            textStyle = textStyles.textStyle,
            textStyleModifiers = textStyleModifiers,
            modifier = modifier
        )
        MarkdownRule -> MarkdownRule(modifier = modifier)
        is MarkdownTable -> MarkdownTable(
            table = node,
            textStyle = textStyles.textStyle,
            textStyleModifiers = textStyleModifiers,
            modifier = modifier
        )
        is MarkdownHtmlBlock -> MarkdownHtmlBlock(
            htmlBlock = node,
            textStyle = textStyles.textStyle,
            modifier = modifier
        )
        is MarkdownUnorderedList -> MarkdownUnorderedList(
            list = node,
            textStyles = textStyles,
            textStyleModifiers = textStyleModifiers,
            blockQuoteStyle = blockQuoteStyle,
            codeBlockStyle = codeBlockStyle,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true, heightDp = 1900)
@Composable
fun MarkdownTextPreview() {
    MarkdownDocument(markdown = """
        # H1        
        H1
        ==
        ## H2
        H2
        --
        ### H3
        #### H4
        ##### H5
        ###### H6

        One line

        Two
        lines

        _italics_
        *italics*

        __bold__
        **bold**

        ~~strikethrough~~

        [anilist link](https://anilist.co/)
        https://anilist.co/
        <https://anilist.co/>

        ![anilist icon](https://anilist.co/img/icons/icon.svg)
        ![image](https://picsum.photos/50)

        ---
        ***

        > block quote
        
        >> nested block quote

        table | with | content
        --- | --- | ---
        content | content | content

        - Bulleted
        - List

        * Bulleted
        * List

        + Bulleted
        + List

        + Nested
          + List

        1. Numbered
        2. List

        6. Numbered
        9. List

        * Mixed
            1. List
                + Containing
            2. Different
        * Types

        `inline code`

            block
            code

        ```
        block
        code
        ```
    """.trimIndent(),
        textStyles = m3TextStyles(),
        textStyleModifiers = m3TextStyleModifiers(),
        blockQuoteStyle = m3BlockQuoteStyle(),
        codeBlockStyle = m3CodeBlockStyle(),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp))
}
