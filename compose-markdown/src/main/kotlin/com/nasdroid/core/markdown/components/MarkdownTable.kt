package com.nasdroid.core.markdown.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.nasdroid.core.markdown.generator.MarkdownTable
import com.nasdroid.core.markdown.style.TextStyleModifiers

/**
 * Displays a [MarkdownTable]. A table is a grid of labelled rows and columns that contain
 * paragraphs.
 */
@Composable
fun MarkdownTable(
    table: MarkdownTable,
    textStyle: TextStyle,
    textStyleModifiers: TextStyleModifiers,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        // Add headers
        Row {
            table.columns.forEach {
                MarkdownParagraph(
                    paragraph = it.header,
                    textStyle = textStyle,
                    textStyleModifiers = textStyleModifiers,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        MarkdownRule()
        table.columns.first().cells.forEachIndexed { index, _ ->
            Row {
                table.columns.forEach {
                    MarkdownParagraph(
                        paragraph = it.cells[index],
                        textStyle = textStyle,
                        textStyleModifiers = textStyleModifiers,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            if (index < table.columns.first().cells.lastIndex) {
                MarkdownRule()
            }
        }
    }
}
