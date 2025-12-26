package com.imashnake.animite.core.ui.layouts.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.imashnake.animite.core.R
import com.imashnake.animite.core.ui.LocalPaddings

/**
 * Most screens and pages follow a banner-style layout in Animite.
 *
 * @param banner A banner [Composable] that is usually an image with
 * [com.imashnake.animite.core.extensions.bannerParallax] and other components.
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
