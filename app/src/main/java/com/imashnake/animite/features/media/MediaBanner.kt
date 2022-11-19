package com.imashnake.animite.features.media

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imashnake.animite.R
import com.imashnake.animite.dev.internal.Constants

@Composable
fun MediaBanner(
    imageUrl: String?,
    tintColor: Color,
    modifier: Modifier = Modifier
) {
    if (!imageUrl.isNullOrEmpty()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(Constants.CROSSFADE_DURATION)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            alignment = Alignment.Center,
            colorFilter = ColorFilter.tint(
                color = tintColor,
                blendMode = BlendMode.SrcAtop
            )
        )
    } else {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            alignment = Alignment.TopCenter,
            colorFilter = ColorFilter.tint(
                color = tintColor,
                blendMode = BlendMode.SrcAtop
            )
        )
    }
}