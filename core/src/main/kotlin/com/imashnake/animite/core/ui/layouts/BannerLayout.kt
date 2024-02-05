package com.imashnake.animite.core.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.imashnake.animite.core.R
import com.imashnake.animite.core.ui.LocalPaddings

@Composable
fun BannerLayout(
    banner: @Composable (Modifier) -> Unit,
    content: @Composable (bannerHeight: Dp) -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    bannerHeight: Dp = dimensionResource(R.dimen.banner_height)
) {
    Box(modifier) {
        banner(
            Modifier
                .height(bannerHeight)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = bannerHeight)
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = LocalPaddings.current.large)
                .navigationBarsPadding()
                .then(contentModifier),
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.large)
        ) { content(bannerHeight) }
    }
}
