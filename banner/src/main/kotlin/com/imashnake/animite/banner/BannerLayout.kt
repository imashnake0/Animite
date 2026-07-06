package com.imashnake.animite.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.util.fastCoerceIn
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
    banner: @Composable BoxScope.(Float, Modifier) -> Unit,
    bannerElevatedContent: @Composable BoxScope.(Float) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    maxBannerHeight: Dp = dimensionResource(R.dimen.banner_height),
    contentPadding: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(LocalPaddings.current.large)
) {
    val density = LocalDensity.current
    var bannerHeightPx by remember { mutableFloatStateOf(with(density) { maxBannerHeight.toPx() }) }
    var ratio by remember { mutableFloatStateOf(1f) }
    val statusBarHeight = with(density) { WindowInsets.statusBars.getTop(density).toDp() }
    val minBannerHeightPx = with(density) { (statusBarHeight + LocalPaddings.current.large).toPx() }
    val maxBannerHeightPx = with(density) { maxBannerHeight.toPx() }

    // Copied from TopAppBar.ExitUntilCollapsedScrollBehavior
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Don't intercept if scrolling down.
                if (available.y > 0f) return Offset.Zero

                val prevBannerHeight = bannerHeightPx
                bannerHeightPx = (bannerHeightPx + available.y).fastCoerceIn(minBannerHeightPx, maxBannerHeightPx)
                ratio = (bannerHeightPx - minBannerHeightPx) / (maxBannerHeightPx - minBannerHeightPx)
                return if (prevBannerHeight != bannerHeightPx) {
                    // We're in the middle of top app bar collapse or expand.
                    // Consume only the scroll on the Y axis.
                    available.copy(x = 0f)
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                if (available.y < 0f || consumed.y < 0f) {
                    // When scrolling up, just update the state's height offset.
                    val oldBannerHeight = bannerHeightPx
                    bannerHeightPx = (bannerHeightPx + consumed.y).fastCoerceIn(minBannerHeightPx, maxBannerHeightPx)
                    ratio = (bannerHeightPx - minBannerHeightPx) / (maxBannerHeightPx - minBannerHeightPx)

                    return Offset(0f, bannerHeightPx - oldBannerHeight)
                }

                if (available.y > 0f) {
                    // Adjust the height offset in case the consumed delta Y is less than what was
                    // recorded as available delta Y in the pre-scroll.
                    val oldBannerHeight = bannerHeightPx
                    bannerHeightPx = (bannerHeightPx + available.y).fastCoerceIn(minBannerHeightPx, maxBannerHeightPx)
                    ratio = (bannerHeightPx - minBannerHeightPx) / (maxBannerHeightPx - minBannerHeightPx)

                    return Offset(0f, bannerHeightPx - oldBannerHeight)
                }
                return Offset.Zero
            }
        }
    }

    Box(modifier.nestedScroll(nestedScrollConnection)) {
        banner(
            ratio,
            Modifier
                .height(with(density) { bannerHeightPx.toDp() })
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = with(density) { bannerHeightPx.toDp() })
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding),
            verticalArrangement = verticalArrangement
        ) { content() }
        bannerElevatedContent(ratio)
    }
}
