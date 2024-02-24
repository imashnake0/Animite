package com.imashnake.animite.core.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.boswelja.markdown.style.BlockQuoteStyle

/**
 * Constructs a better [BlockQuoteStyle].
 */
@Composable
fun animiteBlockQuoteStyle(
    background: Color = MaterialTheme.colorScheme.surfaceVariant,
    barWidth: Dp = 3.dp,
    barColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    barShape: Shape = RectangleShape,
    innerPadding: PaddingValues = PaddingValues(8.dp)
) = BlockQuoteStyle(
    background = background,
    shape = RoundedCornerShape(barWidth),
    barShape = barShape,
    barWidth = barWidth,
    barColor = barColor,
    innerPadding = innerPadding
)
