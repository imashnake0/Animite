package com.imashnake.animite.features.media

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imashnake.animite.R
import com.imashnake.animite.dev.internal.Constants

@Composable
fun MediaTrailer(
    trailer: Trailer,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.trailer),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.size(dimensionResource(R.dimen.medium_padding)))

        val context = LocalContext.current
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.trailer_corner_radius)))
                .clickable {
                    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailer.link))
                    context.startActivity(appIntent)
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(trailer.thumbnail)
                    .crossfade(Constants.CROSSFADE_DURATION)
                    .build(),
                contentDescription = stringResource(R.string.trailer),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.778f) // 16 : 9
                    .clip(
                        RoundedCornerShape(dimensionResource(R.dimen.trailer_corner_radius))
                    ),
                alignment = Alignment.Center
            )

            Image(
                painter = painterResource(R.drawable.youtube),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
