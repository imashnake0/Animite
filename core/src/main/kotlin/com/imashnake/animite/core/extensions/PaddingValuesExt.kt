@file:Suppress("FunctionName", "unused")

package com.imashnake.animite.core.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Stable
fun Paddings(all: Dp) = PaddingValuesExt(all, all, all, all)

@Stable
fun Paddings(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp
) = PaddingValuesExt(horizontal, vertical, horizontal, vertical)

@Stable
fun Paddings(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
) = PaddingValuesExt(start, top, end, bottom)

class PaddingValuesExt(
    @Stable
    val start: Dp = 0.dp,
    @Stable
    val top: Dp = 0.dp,
    @Stable
    val end: Dp = 0.dp,
    @Stable
    val bottom: Dp = 0.dp,
) : PaddingValues {
    init {
        require(start.value >= 0) { "Start padding must be non-negative" }
        require(top.value >= 0) { "Top padding must be non-negative" }
        require(end.value >= 0) { "End padding must be non-negative" }
        require(bottom.value >= 0) { "Bottom padding must be non-negative" }
    }

    override fun calculateLeftPadding(layoutDirection: LayoutDirection) =
        if (layoutDirection == LayoutDirection.Ltr) start else end

    override fun calculateTopPadding() = top

    override fun calculateRightPadding(layoutDirection: LayoutDirection) =
        if (layoutDirection == LayoutDirection.Ltr) end else start

    override fun calculateBottomPadding() = bottom

    override fun equals(other: Any?): Boolean {
        if (other !is PaddingValuesExt) return false
        return start == other.start &&
                top == other.top &&
                end == other.end &&
                bottom == other.bottom
    }

    override fun hashCode() =
        ((start.hashCode() * 31 + top.hashCode()) * 31 + end.hashCode()) * 31 + bottom.hashCode()

    override fun toString() = "PaddingValues(start=$start, top=$top, end=$end, bottom=$bottom)"

    /**
     * Add paddings without regard to the current [LayoutDirection].
     */
    operator fun plus(other: PaddingValuesExt): PaddingValuesExt {
        return Paddings(
            start = start + other.start,
            top = top + other.top,
            end = end + other.end,
            bottom = bottom + other.bottom,
        )
    }
}
