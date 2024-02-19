package com.nasdroid.core.markdown.style

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Describes how a Markdown block quote should appear.
 *
 * @property background The color of the code block background.
 * @property shape The shape of the code block.
 * @property barWidth Width of the bar that appears before the quote.
 * @property barColor Color of the bar that appears before the quote.
 * @property barShape Shape of the bar that appears before the quote.
 * @property innerPadding The padding between the edge of the block quote bounds and the content
 * inside it.
 */
data class BlockQuoteStyle(
    val background: Color,
    val shape: Shape,
    val barWidth: Dp,
    val barColor: Color,
    val barShape: Shape,
    val innerPadding: PaddingValues
)
