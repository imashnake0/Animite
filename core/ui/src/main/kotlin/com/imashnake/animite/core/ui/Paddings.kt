package com.imashnake.animite.core.ui

import androidx.annotation.StringRes
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Paddings(
    val ultraTiny: Dp,
    val tiny: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp
) {
    companion object {
        val ZERO = 0.dp
    }
}

@Immutable
data class PaddingFloats(
    val tiny: Float,
    val small: Float,
    val medium: Float,
    val large: Float,
)

val LocalPaddings = staticCompositionLocalOf<Paddings> { error("Paddings must be set!") }

@Composable
fun rememberDefaultPaddings(density: Density = Density.COMFY): Paddings {
    val densityAnim by animateValueAsState(
        targetValue = when(density) {
            Density.COMFY -> PaddingFloats(
                tiny = 4f,
                small = 8f,
                medium = 16f,
                large = 24f,
            )
            Density.COZY -> PaddingFloats(
                tiny = 3f,
                small = 6f,
                medium = 12f,
                large = 18f,
            )
            Density.COMPACT -> PaddingFloats(
                tiny = 2f,
                small = 4f,
                medium = 8f,
                large = 12f,
            )
        },
        typeConverter = TwoWayConverter(
            convertToVector = {
                AnimationVector4D(
                    it.tiny,
                    it.small,
                    it.medium,
                    it.large,
                )
            },
            convertFromVector = {
                PaddingFloats(
                    tiny = it.v1,
                    small = it.v2,
                    medium = it.v3,
                    large = it.v4,
                )
            }
        ),
        animationSpec = tween(750)
    )
    return Paddings(
        ultraTiny = 1.dp,
        tiny = densityAnim.tiny.dp,
        small = densityAnim.small.dp,
        medium = densityAnim.medium.dp,
        large = densityAnim.large.dp,
    )
}

enum class Density(@param:StringRes val res: Int) {
    COMFY(R.string.comfy),
    COZY(R.string.cozy),
    COMPACT(R.string.compact),
}
