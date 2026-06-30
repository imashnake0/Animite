package com.imashnake.animite.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imashnake.animite.core.ui.LocalPaddings

/**
 * Most screens and pages follow a banner-style layout in Animite.
 *
 * @param banner A banner [Composable] that is usually an image with
 * [com.imashnake.animite.core.ui.ext.bannerParallax] and other components.
 * @param content The content that appears in a [Column] below the banner.
 * @param modifier Modifier for [BannerLayout].
 * @param bannerHeight The height of the banner in [Dp]s.
 * @param bannerModifier Modifier for [banner]. Use this if a [Composable] in [banner] should have
 * the dimensions of the banner.
 * @param contentPadding Padding for [content].
 */
@Composable
fun BannerLayout(
    banner: @Composable (Modifier) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    bannerHeight: Dp = dimensionResource(R.dimen.banner_height),
    bannerModifier: Modifier = Modifier
        .height(bannerHeight)
        .fillMaxWidth(),
    contentPadding: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(LocalPaddings.current.large)
) {
    Box(modifier) {
        banner(bannerModifier)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = bannerHeight)
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding),
            verticalArrangement = verticalArrangement
        ) { content() }
    }
}

@Composable
fun NestedScrollBannerLayout(
    banner: @Composable (Float, Modifier) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    maxBannerHeight: Dp = dimensionResource(R.dimen.banner_height),
    contentPadding: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(LocalPaddings.current.large)
) {
    var bannerHeight by remember { mutableStateOf(maxBannerHeight) }
    var ratio by remember { mutableFloatStateOf(1f) }
    val density = LocalDensity.current
    val statusBarHeight = with(density) { WindowInsets.statusBars.getTop(density).toDp() }
    val minBannerHeight = statusBarHeight + LocalPaddings.current.large

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Calculate the change in image size based on scroll delta
                val delta = available.y / 5
                val newImageSize = bannerHeight + delta.dp
                val previousImageSize = bannerHeight

                // Constrain the image size within the allowed bounds
                bannerHeight = newImageSize.coerceIn(minBannerHeight, maxBannerHeight)
                val consumed = bannerHeight - previousImageSize

                // Calculate the scale for the image
                ratio = (bannerHeight - minBannerHeight) / (maxBannerHeight - minBannerHeight)

                // Return the consumed scroll amount
                return Offset(0f, consumed.value)
            }
        }
    }

    Box(modifier.nestedScroll(nestedScrollConnection)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = bannerHeight)
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding),
            verticalArrangement = verticalArrangement
        ) { content() }
        banner(
            ratio,
            Modifier
                .height(bannerHeight)
                .fillMaxWidth()
        )
    }
}
