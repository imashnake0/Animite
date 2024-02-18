package com.nasdroid.core.markdown.style

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * Describes how a Markdown block quote should appear.
 *
 * @property background The color of the code block background.
 * @property shape The shape of the code block.
 * @property innerPadding The padding between the edge of the block quote bounds and the content
 * inside it.
 */
data class BlockQuoteStyle(
    val background: Color,
    val shape: Shape,
    val innerPadding: PaddingValues
)
