package com.imashnake.animite.core.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun Modifier.scrim(
    color: Color,
    height: Dp
): Modifier {
    return this.drawWithContent {
        drawContent()
        // Draw the top scrim
        drawRect(
            brush = Brush.verticalGradient(
                listOf(
                    color,
                    Color.Transparent
                )
            ),
            size = Size(
                width = size.width,
                height = height.toPx()
            ),
            blendMode = BlendMode.SrcAtop
        )
        //Draw the bottom scrim
        drawRect(
            brush = Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    color
                )
            ),
            size = Size(
                width = size.width,
                height = height.toPx()
            ),
            topLeft = Offset(0f, size.height - height.toPx()),
            blendMode = BlendMode.SrcAtop
        )
    }
}