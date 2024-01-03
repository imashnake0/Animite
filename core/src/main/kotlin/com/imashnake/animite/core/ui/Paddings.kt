package com.imashnake.animite.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imashnake.animite.core.R

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

val LocalPaddings = staticCompositionLocalOf<Paddings> { error("Paddings must be set!") }

@Composable
fun rememberDefaultPaddings(): Paddings {
    return Paddings(
        ultraTiny = dimensionResource(R.dimen.ultra_tiny_padding),
        tiny = dimensionResource(R.dimen.tiny_padding),
        small = dimensionResource(R.dimen.small_padding),
        medium = dimensionResource(R.dimen.medium_padding),
        large = dimensionResource(R.dimen.large_padding)
    )
}
