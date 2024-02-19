package com.imashnake.animite.core.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nasdroid.core.markdown.style.BlockQuoteStyle

/**
 * Constructs a better [BlockQuoteStyle].
 */
@Composable
fun animiteBlockQuoteStyle(
    background: Color = MaterialTheme.colorScheme.surfaceVariant,
    barWidth: Dp = 2.5f.dp,
    barColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    barShape: Shape = CircleShape,
    paddingAfterBar: Dp = 5f.dp,
    innerPadding: PaddingValues = PaddingValues(8.dp)
) = BlockQuoteStyle(
    background = background,
    shape = RoundedCornerShape(
        topStart = barWidth/2 + innerPadding.calculateTopPadding(),
        topEnd = barWidth/2 + innerPadding.calculateTopPadding(),
        bottomStart = barWidth/2 + innerPadding.calculateBottomPadding(),
        bottomEnd = barWidth/2 + innerPadding.calculateBottomPadding()
    ),
    barShape = barShape,
    barWidth = barWidth,
    barColor = barColor,
    paddingAfterBar = paddingAfterBar,
    innerPadding = innerPadding
)
