@file:Suppress("FunctionName", "unused")

package com.imashnake.animite.core.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Stolen from:
// https://github.com/uragiristereo/Mikansei/blob/main/core/ui/src/main/java/com/uragiristereo/mikansei/core/ui/extension/PaddingValues.kt

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val direction = LocalLayoutDirection.current

    return PaddingValues(
        start = calculateStartPadding(direction) + other.calculateStartPadding(direction),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(direction) + other.calculateEndPadding(direction),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
    )
}

@Composable
@Stable
fun PaddingValues.copy(
    start: Dp = calculateStartPadding(LocalLayoutDirection.current),
    top: Dp = calculateTopPadding(),
    end: Dp = calculateEndPadding(LocalLayoutDirection.current),
    bottom: Dp = calculateBottomPadding(),
): PaddingValues {
    return PaddingValues(
        start = start,
        top = top,
        end = end,
        bottom = bottom,
    )
}

@Stable
val PaddingValues.verticalOnly: PaddingValues
    @Composable
    get() = this.copy(start = 0.dp, end = 0.dp)

@Stable
val PaddingValues.horizontalOnly: PaddingValues
    @Composable
    get() = this.copy(top = 0.dp, bottom = 0.dp)
