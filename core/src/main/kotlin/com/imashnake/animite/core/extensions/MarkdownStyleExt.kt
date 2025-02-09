package com.imashnake.animite.core.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.boswelja.markdown.style.BlockQuoteStyle
import com.boswelja.markdown.style.CodeBlockStyle
import com.imashnake.animite.core.R

@Composable
fun animiteBlockQuoteStyle(
    background: Color = MaterialTheme.colorScheme.surfaceVariant,
    barWidth: Dp = dimensionResource(R.dimen.markdown_block_corner_radius),
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

@Composable
fun animiteCodeBlockStyle(
    background: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = RoundedCornerShape(dimensionResource(R.dimen.markdown_block_corner_radius)),
    innerPadding: PaddingValues = PaddingValues(8.dp)
) = CodeBlockStyle(
    background = background,
    shape = shape,
    innerPadding = innerPadding
)
